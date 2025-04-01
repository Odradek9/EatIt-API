/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eat_it.service;

import eat_it.connectionManager.DBConnectionManager;
import eat_it.model.Review;
import eat_it.resource.CategoriesResource;
import eat_it.resource.ProductsResource;
import eat_it.resource.ReviewsResource;
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
public class ReviewsService {
    
    Connection connection = DBConnectionManager.getConnection();
    
    public ArrayList<URI> getReviews(int prodUid, int catUid, UriInfo uriInfo) throws SQLException{
    
        ArrayList<URI> resultList = new ArrayList<>();
        
        //Statement stmt = connection.createStatement();
        String sql = "select id from reviews where productId = ?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setInt(1, prodUid);
        ResultSet resultSet = stmt.executeQuery();

        while (resultSet.next()){
            /**
            Review review = new Review();
            review.setUid(resultSet.getInt("id"));
            review.setStars(resultSet.getInt("stars"));
            review.setComment(resultSet.getString("comment"));
            review.setCustomerUid(resultSet.getInt("customerId"));

            resultList.add(review);
            */
            
            URI uri = uriInfo.getBaseUriBuilder()
                    .path(CategoriesResource.class)
                    .path(CategoriesResource.class, "getProductsResource")
                    .path(ProductsResource.class, "getReviewsResource")
                    .path(ReviewsResource.class, "getProductReview")
                    .build(catUid, prodUid, resultSet.getInt("id"));
            
            resultList.add(uri);
        }
            
        return resultList;
        
    }

    public int postReview(int prodUid, int catUid, Review review) throws SQLException{
        
        int resultUid = 0;
        
        //Statement stmt = connection.createStatement();
        String sql = "INSERT INTO reviews (`stars`, `comment`, `customerId`, `productId`) VALUES (?, ?, ?, ?)";
        PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        stmt.setInt(1, review.getStars());
        stmt.setString(2, review.getComment());
        stmt.setInt(3, review.getCustomerUid());
        stmt.setInt(4, prodUid);
        stmt.executeUpdate();
        ResultSet keys = stmt.getGeneratedKeys();
        
        while( keys.next()) {
            resultUid = keys.getInt(1);
        }
        
        return resultUid;
    }

    public Review getReview(int prodUid, int catUid, int revUid) throws SQLException{
        
        Review review = new Review();
        
        //Statement stmt = connection.createStatement();
        String sql = "select * from reviews where productId = ?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setInt(1, prodUid);
        ResultSet resultSet = stmt.executeQuery();

        while (resultSet.next()) {
            review.setUid(resultSet.getInt("id"));
            review.setStars(resultSet.getInt("stars"));
            review.setComment(resultSet.getString("comment"));
            review.setCustomerUid(resultSet.getInt("customerId"));
        }
            
        return review;
    }

    public boolean deleteProductReview(int catUid, int prodUid, int revUid) throws SQLException{
        
        boolean result = false;
        
        //Statement stmt = connection.createStatement();
        String sql = "DELETE FROM reviews WHERE id = ?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setInt(1, revUid);
        int updatedRows = stmt.executeUpdate();
        
        if (updatedRows > 0) {
            result = true;
        }
        return result;
    }
    
    public int getCustomerUidOfReview(int revUid) throws SQLException {
        
        int result = 0;
        
        //Statement stmt = connection.createStatement();
        String sql = "select customerId from reviews where id = ?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setInt(1, revUid);
        ResultSet resultSet = stmt.executeQuery();

        while (resultSet.next()) {
            result = resultSet.getInt("customerId");
        }
            
        return result;
    }
    
}
