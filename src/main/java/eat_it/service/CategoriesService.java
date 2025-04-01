/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eat_it.service;

import eat_it.connectionManager.DBConnectionManager;
import eat_it.model.Category;
import eat_it.model.CategoryProductsMap;
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
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author anton
 */
public class CategoriesService {
    
    Connection connection = DBConnectionManager.getConnection();
    ProductsService productsService = new ProductsService();
    
    public ArrayList<URI> getAllCategories(UriInfo uriInfo) throws SQLException{
        
        ArrayList<URI> resultList = new ArrayList<>();
        
        Statement stmt = connection.createStatement();
        ResultSet resultSet = stmt.executeQuery("SELECT id FROM categories");
        
        while (resultSet.next()){
            
            URI uri = uriInfo.getBaseUriBuilder()
                    .path(CategoriesResource.class)
                    .path(CategoriesResource.class, "getCategoryInfo")
                    .build(resultSet.getInt("id"));
            /**
            Category category = new Category();
            category.setUid(resultSet.getInt("id"));
            category.setCategoryName(resultSet.getString("categoryName"));
            category.setImagePath(resultSet.getString("imagePath"));
            */

            resultList.add(uri);
        }

        return resultList;
    }
    
    public Category getCategory(int id) throws SQLException{
        
        Category category = new Category();
        
        //Statement stmt = connection.createStatement();
        String sql = "SELECT categoryName, imagePath FROM categories WHERE id = ?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setInt(1, id);
        ResultSet resultSet = stmt.executeQuery();
        

        while (resultSet.next()) {
            category.setUid(id);
            category.setCategoryName(resultSet.getString("categoryName"));
            category.setImagePath(resultSet.getString("imagePath"));
        }

        return category;
    }
    
    public CategoryProductsMap getBesWorsttSellers(UriInfo uriInfo) throws SQLException{
        
        Statement stmt = connection.createStatement();
        ResultSet resultSetCat = stmt.executeQuery("SELECT id FROM categories");
        
        Map<URI, ArrayList<URI>> map = new HashMap<>();
        
        while (resultSetCat.next()){
            
            int categoryUid = resultSetCat.getInt("id");
            
            ArrayList<Integer> list = productsService.getBestWorstProductOfCat(categoryUid);
            
            URI catUri = uriInfo.getBaseUriBuilder()
                    .path(CategoriesResource.class)
                    .path(CategoriesResource.class, "getCategoryInfo")
                    .build(categoryUid);
            
            ArrayList<URI> listURI = new ArrayList<>();
            if (!list.isEmpty()) {
                URI bestUri = uriInfo.getBaseUriBuilder()
                        .path(CategoriesResource.class)
                        .path(CategoriesResource.class, "getProductsResource")
                        .path(ProductsResource.class, "getCategoryProduct")
                        .build(categoryUid, list.get(0));

                URI worstUri = uriInfo.getBaseUriBuilder()
                        .path(CategoriesResource.class)
                        .path(CategoriesResource.class, "getProductsResource")
                        .path(ProductsResource.class, "getCategoryProduct")
                        .build(categoryUid, list.get(1));
                
                listURI.add(bestUri);
                listURI.add(worstUri);
            }
            
            map.put(catUri, listURI);
        }
        
        CategoryProductsMap result = new CategoryProductsMap();
        result.setMap(map);
        
        return result;
    }
    
    public int getCategoryOfProduct(int productUid) throws SQLException {
        
        String sql = "SELECT categoryId FROM products WHERE id = ?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setInt(1, productUid);
        ResultSet resultSet = stmt.executeQuery();
        
        int result = 0;

        while (resultSet.next()) {
            result = resultSet.getInt("categoryId");
        }

        return result;
    }
    
    public Integer addCategory(Category category) throws SQLException{
        
        int resultUid = 0;
        
        //Statement stmt = connection.createStatement();
        String sql = "INSERT INTO categories (`restaurantId`,`categoryName`,`imagePath`) VALUES (1, ?, ?)";
        PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        stmt.setString(1, category.getCategoryName());
        stmt.setString(2, category.getImagePath());
        int addedRows = stmt.executeUpdate();
        
        if (addedRows == 0) {
            return resultUid;
        }
        
        ResultSet resultSet = stmt.getGeneratedKeys();
        
        while (resultSet.next()) {
            resultUid = resultSet.getInt(1);
        }

        return resultUid;
    }
    
    public boolean modifyCategory(Category category) throws SQLException{
        
        boolean result = false;
        
        //Statement stmt = connection.createStatement();
        String sql = "UPDATE categories SET categoryName = ?, imagePath = ? WHERE id = ?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, category.getCategoryName());
        stmt.setString(2, category.getImagePath());
        stmt.setInt(3, category.getUid());
        int modifiedRows = stmt.executeUpdate();
        
        if (modifiedRows > 0) {
            result = true;
        }

        return result;
    }
    
    public boolean deleteCategory(int categoryUid) throws SQLException{
        
        boolean result = false;
        
        //Statement stmt = connection.createStatement();
        String sql = "DELETE FROM categories WHERE id = ?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setInt(1, categoryUid);
        int removedRows = stmt.executeUpdate();
        
        if (removedRows > 0) {
            result = true;
        }

        return result;
    }
}