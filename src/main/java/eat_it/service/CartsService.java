/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eat_it.service;

import eat_it.connectionManager.DBConnectionManager;
import eat_it.model.JsonData;
import eat_it.model.JsonList;
import eat_it.resource.CategoriesResource;
import eat_it.resource.ProductsResource;
import jakarta.ws.rs.core.UriInfo;
import java.net.URI;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author anton
 */
public class CartsService {
    
    Connection connection = DBConnectionManager.getConnection();
    CategoriesService categoriesService = new CategoriesService();
    
    public int createCart() throws SQLException {
        
        Statement stmt_pre = connection.createStatement();
        stmt_pre.executeUpdate("insert into carts () values ()", Statement.RETURN_GENERATED_KEYS);
        ResultSet keys = stmt_pre.getGeneratedKeys();
        int cartUid = 0;
        while (keys.next()) {
            cartUid = keys.getInt(1);
        }
        
        return cartUid;
    }
    
    public JsonList getCustomerCart(int customerUid, UriInfo uriInfo) throws SQLException {
        
        String sqlCartId = "select cartId from customers where id = ?";
        String sql = "SELECT pc.productId, pc.quantity, p.categoryId FROM products_carts as pc join products as p on pc.productId = p.id where pc.cartId = (" + sqlCartId + ")";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setInt(1, customerUid);
        
        ArrayList<JsonData> list = new ArrayList<>();
        JsonList result = new JsonList();
        
        //System.out.println(stmt.toString());
        
        ResultSet resultSet = stmt.executeQuery();
        while (resultSet.next()){
            JsonData data = new JsonData();
            
            URI uri = uriInfo.getBaseUriBuilder()
                    .path(CategoriesResource.class)
                    .path(CategoriesResource.class, "getProductsResource")
                    .path(ProductsResource.class, "getCategoryProduct")
                    .build(resultSet.getInt("categoryId"), resultSet.getInt("productId"));
            
            data.setProductURI(uri);
            data.setQuantity(resultSet.getInt("quantity"));
            list.add(data);
        }
        
        result.setList(list);
        return result;
    }
    
    public boolean addProductToCustomerCart(int customerUid, int prodUid, int quantity) throws SQLException{
        
        boolean result = false;
        
        //Statement stmt = connection.createStatement();
        String sqlCartId = "select cartId from customers where id = ?";
        String sql = "INSERT products_carts (`cartId`, `productId`, `quantity`) VALUES ((" + sqlCartId + "), ?, ?)";
        PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        stmt.setInt(1, customerUid);
        stmt.setInt(2, prodUid);
        stmt.setInt(3, quantity);
        stmt.executeUpdate();
        
        ResultSet keys = stmt.getGeneratedKeys();
        result = keys.next();
        
        return result;
    }

    public boolean updateProductInCustomerCart(int customerUid, int prodUid, int quantity) throws SQLException{
        
        boolean result = false;
        
        //Statement stmt = connection.createStatement();
        String sqlCartId = "select cartId from customers where id = ?";
        String sql = "UPDATE products_carts SET quantity = ? WHERE cartId = (" + sqlCartId + ") AND productId = ?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setInt(1, quantity);
        stmt.setInt(2, customerUid);
        stmt.setInt(3, prodUid);
        int updatedRows = stmt.executeUpdate();
        
        if (updatedRows > 0){
            result = true;
        }
        return result;
    }

    public boolean deleteProductFromCart(int customerUid, int prodUid) throws SQLException{
        
        boolean result = false;
        
        //Statement stmt = connection.createStatement();
        String sqlCartId = "select cartId from customers where id = ?";
        String sql = "DELETE FROM products_carts WHERE cartId = (" + sqlCartId + ") AND productId = ?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setInt(1, customerUid);
        stmt.setInt(2, prodUid);
        int deletedRows = stmt.executeUpdate();
        
        if (deletedRows > 0){
            result = true;
        }
        return result;
    }
    
    public boolean addFavouritesToCart(int customerUid) throws SQLException {
        
        Boolean result = false;
        
        String sqlSelect = "SELECT c.cartId, pf.productId, 1 AS quantity FROM customers AS c JOIN  products_favourites AS pf ON c.favId = pf.favId WHERE c.id = ?";
        String sql = "INSERT products_carts (`cartId`, `productId`, `quantity`) " + sqlSelect;
        PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        stmt.setInt(1, customerUid);
        
        stmt.executeUpdate();
        ResultSet resultSet = stmt.getGeneratedKeys();
        
        while(resultSet.next()) {
            result = true;
        }
        return result;
    }

    public float getCartTotal(int customerUid) throws SQLException {
        
        String sql = "SELECT SUM(p.price * pc.quantity) FROM products_carts AS pc JOIN products AS p JOIN customers AS c WHERE pc.productId = p.id AND pc.cartId = c.cartId AND c.id = ?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setInt(1, customerUid);
        ResultSet resultSet = stmt.executeQuery();
        
        float resultFloat = 0;
        
        while (resultSet.next()) {
            resultFloat = resultSet.getFloat(1);
        }
        return resultFloat;
    }

    public boolean createCartFromOrder(int orderUid) throws SQLException {
        
        boolean result = false;
        
        String sqlSelect = "SELECT c.cartId, op.productId, op.quantity FROM orders_products AS op JOIN orders AS o ON o.id = op.orderId JOIN customers AS c ON c.id = o.customerId WHERE o.id = ?";
        String sql = "INSERT products_carts (`cartId`, `productId`, `quantity`) " + sqlSelect;
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setInt(1, orderUid);
        
        int addedRows = stmt.executeUpdate();
        if (addedRows > 0){
            result = true;
        }
        //System.out.println(addedRows + stmt.toString());
        return result;
    }
    
    public boolean isProductInCart(int productUid, int customerUid) throws SQLException {
        
        boolean resultCheck = false;
        
        //Statement stmt = connection.createStatement();
        String sqlCartId = "SELECT cartId FROM customers WHERE id = ?";
        String sql = "SELECT CASE WHEN EXISTS " + 
                "(SELECT id FROM products_carts WHERE productId = ? AND cartId = (" + sqlCartId + ")) " +
                "THEN 'TRUE' ELSE 'FALSE' END";
        System.out.println(sql);
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setInt(1, productUid);
        stmt.setInt(2, customerUid);
        System.out.println(stmt.toString());
        
        ResultSet resultSet = stmt.executeQuery();

        while (resultSet.next()) {
            resultCheck = resultSet.getBoolean(1);
        }

        return resultCheck;
    }
    
}
