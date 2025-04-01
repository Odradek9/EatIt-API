/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eat_it.service;

import eat_it.connectionManager.DBConnectionManager;
import eat_it.model.Product;
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
public class ProductsService {
    
    Connection connection = DBConnectionManager.getConnection();
    
    public ArrayList<URI> getAllProducts(UriInfo uriInfo) throws SQLException{
        
        ArrayList<URI> resultList = new ArrayList<>();
        
        
        Statement stmt = connection.createStatement();
        ResultSet resultSet = stmt.executeQuery("select id, categoryId from products");
        while (resultSet.next()){
            
            /**
            Product product = new Product();
            product.setUid(resultSet.getInt("id"));
            product.setName(resultSet.getString("name"));
            product.setDescription(resultSet.getString("description"));
            product.setPrice(resultSet.getFloat("price"));
            product.setImagePath(resultSet.getString("imagePath"));
            product.setTimesOrdered(resultSet.getInt("timesOrdered"));
            resultList.add(product);
            */
            URI uri = uriInfo.getBaseUriBuilder()
                    .path(CategoriesResource.class)
                    .path(CategoriesResource.class, "getProductsResource")
                    .path(ProductsResource.class, "getCategoryProduct")
                    .build(resultSet.getInt("categoryId"), resultSet.getInt("id"));
            
            resultList.add(uri);
        }
        return resultList;
    
}
    
    public Product getProduct(int prodId, int catId) throws SQLException{

        Product product = new Product();

        //Statement stmt = connection.createStatement();
        String sql = "SELECT * FROM products WHERE id = ? AND categoryId = ?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setInt(1, prodId);
        stmt.setInt(2, catId);
        ResultSet resultSet = stmt.executeQuery();

        while (resultSet.next()) {
            product.setUid(resultSet.getInt("id"));
            product.setName(resultSet.getString("name"));
            product.setDescription(resultSet.getString("description"));
            product.setPrice(resultSet.getFloat("price"));
            product.setImagePath(resultSet.getString("imagePath"));
            product.setTimesOrdered(resultSet.getInt("timesOrdered"));
        }

        return product;
    }
    
    public ArrayList<Product> getProductsByCat(int catId) throws SQLException{
        
        ArrayList<Product> resultList = new ArrayList<Product>();
        
        //Statement stmt = connection.createStatement();
        String sql = "select * from products where categoryId = ?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setInt(1, catId);
        ResultSet resultSet = stmt.executeQuery();

        while (resultSet.next()){
            Product product = new Product();
            product.setUid(resultSet.getInt("id"));
            product.setName(resultSet.getString("name"));
            product.setDescription(resultSet.getString("description"));
            product.setPrice(resultSet.getFloat("price"));
            product.setImagePath(resultSet.getString("imagePath"));
            product.setTimesOrdered(resultSet.getInt("timesOrdered"));

            resultList.add(product);
        }
        return resultList;
    }

    public Integer storeProduct(Product product, int catUid) throws SQLException{
        
        int resultUid = 0;
        
        //Statement stmt = connection.createStatement();
        String sql = "INSERT INTO products (`name`,`description`,`price`,`categoryId`, `imagePath`) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        stmt.setString(1, product.getName());
        stmt.setString(2, product.getDescription());
        stmt.setFloat(3, product.getPrice());
        stmt.setInt(4, catUid);
        stmt.setString(5, product.getImagePath());
        stmt.executeUpdate();
        ResultSet keys = stmt.getGeneratedKeys();
        while (keys.next()) {
            resultUid = keys.getInt(1);
        }
        
        return resultUid;
    }
    
    public boolean updateProduct(Product product, int catUid, int prodUid) throws SQLException{
        
        boolean result = false;
        
        //Statement stmt = connection.createStatement();
        String sql = "UPDATE products SET name = ?, description = ?, price = ?, imagePath = ? WHERE id = ? AND categoryId = ?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, product.getName());
        stmt.setString(2, product.getDescription());
        stmt.setFloat(3, product.getPrice());
        stmt.setString(4, product.getImagePath());
        stmt.setInt(5, product.getUid());
        stmt.setInt(6, catUid);
        int updatedRows = stmt.executeUpdate();
        
        if (updatedRows > 0) {
            result = true;
        }
        return result;
    }
    
    public boolean deleteProduct(int catUid, int prodUid) throws SQLException{
        
        boolean result = false;
        
        //Statement stmt = connection.createStatement();
        String sql = "DELETE FROM products WHERE id = ? AND categoryId = ?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setInt(1, prodUid);
        stmt.setInt(2, catUid);
        int removedRows = stmt.executeUpdate();
        
        if (removedRows > 0) {
            result = true;
        }
        return result;
    }
    
    public ArrayList<Integer> getBestWorstProductOfCat(int catUid) throws SQLException{
        
        //Statement stmt = connection.createStatement();
        
        String sqlBest = "SELECT id FROM products WHERE categoryId = ? ORDER BY timesOrdered DESC LIMIT 1";
        PreparedStatement stmt = connection.prepareStatement(sqlBest);
        stmt.setInt(1, catUid);
        ResultSet resultSetBest = stmt.executeQuery();
        
        String sqlWorst = "SELECT id FROM products WHERE categoryId = ? ORDER BY timesOrdered LIMIT 1";
        stmt = connection.prepareStatement(sqlWorst);
        stmt.setInt(1, catUid);
        ResultSet resultSetWorst = stmt.executeQuery();
        
        ArrayList<Integer> prodArray = new ArrayList<>();
        
        while (resultSetBest.next()) {
            /**
            Product productBest = new Product();
            productBest.setUid();
            productBest.setName(resultSetBest.getString("name"));
            productBest.setDescription(resultSetBest.getString("description"));
            productBest.setPrice(resultSetBest.getFloat("price"));
            productBest.setImagePath(resultSetBest.getString("imagePath"));
            productBest.setTimesOrdered(resultSetBest.getInt("timesOrdered"));
            */
            prodArray.add(resultSetBest.getInt("id"));
        }
        
        while (resultSetWorst.next()) {
            /**
            Product productWorst = new Product();
            productWorst.setUid();
            productWorst.setName(resultSetWorst.getString("name"));
            productWorst.setDescription(resultSetWorst.getString("description"));
            productWorst.setPrice(resultSetWorst.getFloat("price"));
            productWorst.setImagePath(resultSetWorst.getString("imagePath"));
            productWorst.setTimesOrdered(resultSetWorst.getInt("timesOrdered"));
            */
            prodArray.add(resultSetWorst.getInt("id"));
        }
        
        return prodArray;
    }
    
    public ArrayList<URI> getBestWorstProduct(UriInfo uriInfo) throws SQLException{
        
        //Statement stmt = connection.createStatement();
        
        String sqlBest = "SELECT id, categoryId FROM products ORDER BY timesOrdered DESC LIMIT 1";
        PreparedStatement stmt = connection.prepareStatement(sqlBest);
        ResultSet resultSetBest = stmt.executeQuery();
        
        String sqlWorst = "SELECT id, categoryId FROM products ORDER BY timesOrdered LIMIT 1";
        stmt = connection.prepareStatement(sqlWorst);
        ResultSet resultSetWorst = stmt.executeQuery();
        
        ArrayList<URI> prodArray = new ArrayList<>();
        
        while (resultSetBest.next() && resultSetWorst.next()) {
            /**
            Product productBest = new Product();
            productBest.setUid();
            productBest.setName(resultSetBest.getString("name"));
            productBest.setDescription(resultSetBest.getString("description"));
            productBest.setPrice(resultSetBest.getFloat("price"));
            productBest.setImagePath(resultSetBest.getString("imagePath"));
            productBest.setTimesOrdered(resultSetBest.getInt("timesOrdered"));
            */
            
            URI bestUri = uriInfo.getBaseUriBuilder()
                        .path(CategoriesResource.class)
                        .path(CategoriesResource.class, "getProductsResource")
                        .path(ProductsResource.class, "getCategoryProduct")
                        .build(resultSetBest.getInt("categoryId"), resultSetBest.getInt("id"));
            
            URI worstUri = uriInfo.getBaseUriBuilder()
                        .path(CategoriesResource.class)
                        .path(CategoriesResource.class, "getProductsResource")
                        .path(ProductsResource.class, "getCategoryProduct")
                        .build(resultSetWorst.getInt("categoryId"), resultSetWorst.getInt("id"));
            
            prodArray.add(bestUri);
            prodArray.add(worstUri);
        }
        
        return prodArray;
    }
    
}
