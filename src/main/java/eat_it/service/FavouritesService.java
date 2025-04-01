/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eat_it.service;

import eat_it.connectionManager.DBConnectionManager;
import eat_it.model.Product;
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
public class FavouritesService {
    
    Connection connection = DBConnectionManager.getConnection();
    
    public int createFavourites() throws SQLException {
        
        Statement stmt_pre = connection.createStatement();
        
        stmt_pre.executeUpdate("insert into favourites () values ()", Statement.RETURN_GENERATED_KEYS);
        ResultSet keys = stmt_pre.getGeneratedKeys();
        int favUid = 0;
        while (keys.next()) {
            favUid = keys.getInt(1);
        }
        
        return favUid;
        
    }
    
    public ArrayList<Product> getCustomerFavourites(int customerUid) throws SQLException {
        
        String sqlFavId = "select favId from customers where id = ?";
        String sql = "SELECT * FROM products_favourites as pf join products as p on pf.productId = p.id where pf.favId = (" + sqlFavId + ")";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setInt(1, customerUid);
        
        ArrayList<Product> resultList = new ArrayList<Product>();
        
        ResultSet resultSet = stmt.executeQuery();
        while (resultSet.next()){
            Product product = new Product();
            product.setUid(resultSet.getInt("id"));
            product.setName(resultSet.getString("name"));
            product.setDescription(resultSet.getString("description"));
            product.setPrice(resultSet.getFloat("price"));
            product.setImagePath(resultSet.getString("imagePath"));
            resultList.add(product);
        }
        return resultList;
    }
    
    public boolean addProductToFavourites(int customerUid, int prodUid) throws SQLException{
        
        boolean result = false;
        
        //Statement stmt = connection.createStatement();
        String sqlFavId = "select favId from customers where id = ?";
        String sql = "INSERT INTO products_favourites (`favId`,`productId`) VALUES ((" + sqlFavId + "), ?)";
        PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        stmt.setInt(1, customerUid);
        stmt.setInt(2, prodUid);
        stmt.executeUpdate();
        ResultSet keys = stmt.getGeneratedKeys();
        result = keys.next();
        
        return result;
    }
    
    public boolean deleteProductFromFavourites(int customerUid, int prodUid) throws SQLException{
        
        boolean result = false;
        //Statement stmt = connection.createStatement();
        String sqlFavId = "select favId from customers where id = ?";
        String sql = "DELETE FROM products_favourites WHERE favId = (" + sqlFavId + ") AND productId = ?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setInt(1, customerUid);
        stmt.setInt(2, prodUid);
        int deletedRows = stmt.executeUpdate();
        
        if (deletedRows > 0) {
            result = true;
        }
        return result;
    }

    
}
