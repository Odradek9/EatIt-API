/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eat_it.service;

import eat_it.connectionManager.DBConnectionManager;
import eat_it.model.JsonData;
import eat_it.model.Order;
import eat_it.resource.OrdersResource;
import jakarta.ws.rs.core.UriInfo;
import java.net.URI;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author anton
 */
public class OrdersService {

    Connection connection = DBConnectionManager.getConnection();
    CustomersService customersService = new CustomersService();
    CouponsService couponsService = new CouponsService();
    
    public int createOrder(int customerUid, Integer addressUid, String couponUid, Integer cardUid, float total) throws SQLException {
        
        int orderUid = 0;
        
        String sql = "INSERT INTO orders(`creationDate`, `total`, `arrivalTime`, `couponId`, `customerId`, `paymentId`, `orderState`, `addressId`, `cardId`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        
        LocalDate date = LocalDate.now(ZoneId.systemDefault());
        stmt.setString(1, date.toString());
        stmt.setFloat(2, total);
        stmt.setString(3, null);
        if (couponUid != null) {
            if(couponsService.isCouponValid(couponUid)) {
                stmt.setString(4, couponUid);
                couponsService.setCouponToUsed(couponUid);
            }
            else{
                orderUid = -5;
                return orderUid;
            }
        }
        else {
            stmt.setString(4, null);
        }
        stmt.setInt(5, customerUid);
        if (cardUid != null) {
            if (cardUid != 0) {
                stmt.setInt(6, 2);
                stmt.setInt(9, cardUid);
            }
        }
        else {
            stmt.setInt(6, 1);
            stmt.setString(9, null);
        }
        stmt.setString(7, "Pending");
        if (addressUid != 0 && customersService.isAddressOfCustomer(customerUid, addressUid)) {
            stmt.setInt(8, addressUid);
        }
        
        stmt.executeUpdate();
        ResultSet keys = stmt.getGeneratedKeys();
        while (keys.next()) {
            orderUid = keys.getInt(1);
        }
        
        return orderUid;
    }
    
    public Order getOrder(int orderUid) throws SQLException {
        
        String sql = "SELECT * FROM orders WHERE id = ?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setInt(1, orderUid);
        ResultSet resultSet = stmt.executeQuery();
        
        Order order = new Order();
        while (resultSet.next()) {
            order.setUid(orderUid);
            order.setAddressUid(resultSet.getInt("addressId"));
            Date date = new Date();
            order.setArrivalTime(LocalTime.parse(resultSet.getString("arrivalTime")));
            order.setCouponUid(resultSet.getInt("couponId"));
            order.setCreationDate(LocalDate.parse(resultSet.getString("creationDate")));
            order.setCustomerUid(resultSet.getInt("customerId"));
            int payment = resultSet.getInt("paymentId");
            if (payment == 2) {
                order.setPaymentUid(resultSet.getInt("cardId"));
            }
            order.setTotal(resultSet.getFloat("total"));
            order.setState(resultSet.getString("orderState"));
        }
        
        return order;
    }
    
    public ArrayList<URI> getOrders(UriInfo uriInfo) throws SQLException {
        
        String sql = "SELECT * FROM orders";
        PreparedStatement stmt = connection.prepareStatement(sql);
        ResultSet resultSet = stmt.executeQuery();
        
        ArrayList<URI> resultList = new ArrayList<>();
        
        //Order order = new Order();
        while (resultSet.next()) {
            /**
            order.setUid(orderUid);
            order.setAddressUid(resultSet.getInt("addressId"));
            order.setArrivalTime(LocalDateTime.parse(resultSet.getString("arrivalTime")));
            order.setCouponUid(resultSet.getInt("couponId"));
            order.setCreationDate(LocalDate.parse(resultSet.getString("creationDate")));
            order.setCustomerUid(resultSet.getInt("customerId"));
            int payment = resultSet.getInt("paymentId");
            if (payment == 2) {
                order.setPaymentUid(resultSet.getInt("cardId"));
            }
            order.setTotal(resultSet.getFloat("total"));
            order.setState(resultSet.getString("orderState"));
            */
            
            URI uri = uriInfo.getBaseUriBuilder()
                    .path(OrdersResource.class)
                    .path(OrdersResource.class, "getOrder")
                    .build(resultSet.getInt("id"));
            resultList.add(uri);
        }
        
        return resultList;
    }

    public boolean acceptOrder(int orderUid) throws SQLException {
        
        boolean result = false;
        
        String sql = "UPDATE orders SET orderState = 'Accepted' WHERE id = ?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setInt(1, orderUid);
        
        int updatedRows = stmt.executeUpdate();
        if (updatedRows > 0){
            result = true;
        }
        return result;
    }

    public boolean refuseOrder(int orderUid) throws SQLException {
        
        boolean result = false;
        
        String sql = "UPDATE orders SET orderState = 'Denied' WHERE id = ?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setInt(1, orderUid);
        
        int updatedRows = stmt.executeUpdate();
        if (updatedRows > 0){
            result = true;
        }
        return result;
    }

    public boolean confirmOrder(int orderUid) throws SQLException {
        
        boolean result = false;
        
        String sql = "UPDATE orders SET orderState = 'Completed' WHERE id = ?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setInt(1, orderUid);
        
        int updatedRows = stmt.executeUpdate();
        if (updatedRows > 0){
            result = true;
        }
        return result;
    }

    public ArrayList<JsonData> getOrderProducts(int orderUid) throws SQLException {
        
        String sql = "SELECT productId, quantity FROM orders_products as op join products as p on op.productId = p.id where op.orderId = ?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setInt(1, orderUid);
        
        ArrayList<JsonData> resultList = new ArrayList<>();
        
        ResultSet resultSet = stmt.executeQuery();
        while (resultSet.next()){
            JsonData data = new JsonData();
            data.setProductUid(resultSet.getInt("productId"));
            data.setQuantity(resultSet.getInt("quantity"));
            resultList.add(data);
        }
        return resultList;
    }
    
}
