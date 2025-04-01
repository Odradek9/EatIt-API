/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eat_it.service;

import eat_it.connectionManager.DBConnectionManager;
import eat_it.model.Customer;
import eat_it.resource.CustomersResource;
import jakarta.ws.rs.core.UriInfo;
import java.net.URI;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import org.springframework.security.crypto.bcrypt.BCrypt;

/**
 *
 * @author anton
 */
public class CustomersService {
    
    Connection connection = DBConnectionManager.getConnection();
    FavouritesService favouritesService = new FavouritesService();
    CartsService cartsService = new CartsService();
    
    public String addCustomer(Customer customer) throws SQLException{
        
        int favUid = favouritesService.createFavourites();
        int cartUid = cartsService.createCart();
        
        if (favUid == 0 || cartUid == 0) {
            return null;
        }
        String sql = "INSERT INTO customers (`name`, `surname`, `email`, `password`, `favId`, `cartId`, `imagePath`) values (?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        stmt.setString(1, customer.getName());
        stmt.setString(2, customer.getSurname());
        stmt.setString(3, customer.getEmail());
        stmt.setString(4, BCrypt.hashpw(customer.getPassword(), BCrypt.gensalt()));
        stmt.setInt(5, favUid);
        stmt.setInt(6, cartUid);
        stmt.setString(7, customer.getImagePath());
        
        stmt.executeUpdate();
        ResultSet keys = stmt.getGeneratedKeys();
        
        String resultUid = null;
        while (keys.next()) {
            resultUid = String.valueOf(keys.getInt(1));
        }
        
        return resultUid;
        
    }
    
    public Customer getCustomer(int customerUid) throws SQLException{
        
        //Statement stmt = connection.createStatement();
        String sql = "SELECT * FROM customers WHERE id = ?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setInt(1, customerUid);
        ResultSet resultSet = stmt.executeQuery();
        
        Customer customer = new Customer();
        while (resultSet.next()) {
            customer.setUid(resultSet.getInt("id"));
            customer.setName(resultSet.getString("name"));
            customer.setSurname(resultSet.getString("surname"));
            customer.setEmail(resultSet.getString("email"));
            customer.setFavUid(resultSet.getInt("favId"));
            customer.setCartUid(resultSet.getInt("cartId"));
            customer.setImagePath(resultSet.getString("imagePath"));
        }
        
        return customer;
    }
    
    public ArrayList<URI> getCustomers(UriInfo uriInfo) throws SQLException{
        
        //Statement stmt = connection.createStatement();
        String sql = "SELECT id FROM customers";
        PreparedStatement stmt = connection.prepareStatement(sql);
        ResultSet resultSet = stmt.executeQuery();
        
        ArrayList<URI> result = new ArrayList<>();
        
        while (resultSet.next()) {
            /**
            Customer customer = new Customer();
            customer.setUid(resultSet.getInt("id"));
            customer.setName(resultSet.getString("name"));
            customer.setSurname(resultSet.getString("surname"));
            customer.setEmail(resultSet.getString("email"));
            customer.setFavUid(resultSet.getInt("favId"));
            customer.setCartUid(resultSet.getInt("cartId"));
            customer.setImagePath(resultSet.getString("imagePath"));
            result.add(customer);
            */
            
            URI uri = uriInfo.getBaseUriBuilder()
                    .path(CustomersResource.class)
                    .path(CustomersResource.class, "getCustomer")
                    .build(resultSet.getInt("id"));
            
            result.add(uri);
        }
        
        return result;
    }
    
    public ArrayList<Customer> getBestCustomersOfMonth() throws SQLException{
        
        //Statement stmt = connection.createStatement();
        String sql = "SELECT customers.id, customers.name, customers.surname, customers.email, sum(orders.total) as total from orders join customers on customers.id = orders.customerId where year(creationDate) = year(current_date()) and orders.orderState='Completed' and month(creationDate) = ? group by customers.id order by total DESC limit 10";
        PreparedStatement stmt = connection.prepareStatement(sql);
        
        LocalDate currentDate = LocalDate.now();
        LocalDate previousMonth = currentDate.minusMonths(1);
        
        stmt.setString(1, previousMonth.toString());
        ResultSet resultSet = stmt.executeQuery();
        
        ArrayList<Customer> result = new ArrayList<>();
        
        while (resultSet.next()) {
            Customer customer = new Customer();
            customer.setUid(resultSet.getInt("id"));
            customer.setName(resultSet.getString("name"));
            customer.setSurname(resultSet.getString("surname"));
            customer.setEmail(resultSet.getString("email"));
            customer.setTotal(resultSet.getFloat("total"));
            
            result.add(customer);
        }
        
        return result;
    }
    
    public boolean authUser(String email, String password) throws SQLException{
        
        //Statement stmt = connection.createStatement();
        String sql = "SELECT password FROM customers where email = ? UNION SELECT password FROM restaurant where email = ?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, email);
        stmt.setString(2, email);
        ResultSet resultSet = stmt.executeQuery();

        resultSet.next();

        return BCrypt.checkpw(password, resultSet.getString("password"));
    }
    
    public boolean isUserAdmin(String email) throws SQLException{
        
        boolean resultCheck = false;
        
        //Statement stmt = connection.createStatement();
        String sql = "SELECT CASE WHEN EXISTS " + 
                "(SELECT name FROM restaurant WHERE id = 1 and email = ?) " +
                "THEN 'TRUE' ELSE 'FALSE' END";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, email);
        ResultSet resultSet = stmt.executeQuery();

        while (resultSet.next()) {
            resultCheck = resultSet.getBoolean(1);
        }

        return resultCheck;
    }
    
    public boolean isUserCustomer(String email) throws SQLException{
        
        //Statement stmt = connection.createStatement();
        String sql = "SELECT CASE WHEN EXISTS " + 
                "(SELECT name FROM customers WHERE and email = ?) " +
                "THEN 'TRUE' ELSE 'FALSE' END";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, email);
        ResultSet resultSet = stmt.executeQuery();

        while (resultSet.next()) {
            return resultSet.getBoolean(1);
        }
        return false;
    }
    
    public int getCustomerUid(String email) throws SQLException{
        
        int resultUid = 0;
        
        //Statement stmt = connection.createStatement();
        String sql = "SELECT id FROM customers where email = ?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, email);
        ResultSet resultSet = stmt.executeQuery();

        while (resultSet.next()) {
            resultUid = resultSet.getInt("id");
        }

        return resultUid;
    }
    
    public boolean isAddressOfCustomer(int customerUid, int addressUid) throws SQLException {
        
        String sql = "SELECT CASE WHEN EXISTS " + 
                "(SELECT street FROM shippingaddresses WHERE id = ? and customerId = ?) " +
                "THEN 'TRUE' ELSE 'FALSE' END";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setInt(1, addressUid);
        stmt.setInt(2, customerUid);
        ResultSet resultSet = stmt.executeQuery();
        
        resultSet.next();

        return resultSet.getBoolean(1);
    }
    
    public boolean isOrderOfCustomer(int customerUid, int orderUid) throws SQLException {
        
        String sql = "SELECT CASE WHEN EXISTS " + 
                "(SELECT id FROM orders WHERE id = ? and customerId = ?) " +
                "THEN 'TRUE' ELSE 'FALSE' END";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setInt(1, orderUid);
        stmt.setInt(2, customerUid);
        ResultSet resultSet = stmt.executeQuery();
        
        resultSet.next();

        return resultSet.getBoolean(1);
    }

}
