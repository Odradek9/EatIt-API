/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eat_it.service;

import eat_it.connectionManager.DBConnectionManager;
import eat_it.model.Coupon;
import eat_it.resource.CouponsResource;
import jakarta.ws.rs.core.UriInfo;
import java.net.URI;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.UUID;

/**
 *
 * @author anton
 */
public class CouponsService {
    
    Connection connection = DBConnectionManager.getConnection();

    public String createNewCoupon(Coupon coupon) throws SQLException {
        
        String resultUid = null;
        
        Date expDate = Date.valueOf(coupon.getExpirationDate());
        Date now = Date.valueOf(LocalDate.now(ZoneId.systemDefault()));
        if(expDate.after(now)) {
            
            String sql = "INSERT INTO coupons (`customerId`, `priceCut`, `expirationDate`, `isUsed`, `id`) VALUES (?, ?, ?, 0, ?)";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, coupon.getCustomerUid());
            stmt.setInt(2, coupon.getPriceCut());
            stmt.setDate(3, Date.valueOf(coupon.getExpirationDate()));
            
            String randomHex = UUID.randomUUID().toString().replace("-", "");
            String newUid = "C" + randomHex.substring(0, 12);
            //System.out.println(newUid);
            
            stmt.setString(4, newUid);
            
            int addedRows = stmt.executeUpdate();
            
            if(addedRows > 0) {
                resultUid = newUid;
            }
            
        }
        
        return resultUid;
    }

    public Coupon getCoupon(String couponUid) throws SQLException {
        
        Coupon coupon = new Coupon();
        
        String sql = "SELECT * FROM coupons WHERE id = ?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, couponUid);
        
        ResultSet resultSet = stmt.executeQuery();
        while (resultSet.next()) {
            
            coupon.setCustomerUid(resultSet.getInt("customerId"));
            coupon.setExpirationDate(LocalDate.parse(resultSet.getDate("expirationDate").toString()));
            coupon.setPriceCut(resultSet.getInt("priceCut"));
            coupon.setIsUsed(resultSet.getInt("isUsed"));
            coupon.setUid(resultSet.getString("id"));
        }
        
        return coupon;
    }
    
    public boolean setCouponToUsed(String couponUid) throws SQLException {
        
        boolean result = false;
        
        String sql = "UPDATE coupons SET isUsed = 1 WHERE id = ?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, couponUid);
        int updatedRows = stmt.executeUpdate();
        
        if (updatedRows > 0) {
            result = true;
        }
        return result;
    }
    
    public boolean isCouponValid(String couponUid) throws SQLException {
        
        String sql = "SELECT CASE WHEN EXISTS " + 
                "(SELECT id FROM coupons WHERE id = ? AND isUsed = 0 AND expirationDate < CURRENT_DATE()) " +
                "THEN 'TRUE' ELSE 'FALSE' END";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, couponUid);
        ResultSet resultSet = stmt.executeQuery();

        resultSet.next();

        return resultSet.getBoolean(1);
    }

    public ArrayList<URI> getCoupons(UriInfo uriInfo) throws SQLException {
        
        ArrayList<URI> resultList = new ArrayList<>();
        
        String sql = "SELECT id FROM coupons WHERE isUsed = 0 AND expirationDate < CURRENT_DATE()";
        PreparedStatement stmt = connection.prepareStatement(sql);
        
        ResultSet resultSet = stmt.executeQuery();
        while (resultSet.next()) {
            /**
            Coupon coupon = new Coupon();
            coupon.setCustomerUid(resultSet.getInt("customerUid"));
            coupon.setExpirationDate(LocalDate.parse(resultSet.getDate("expirationDate").toString()));
            coupon.setPriceCut(resultSet.getInt("priceCut"));
            coupon.setIsUsed(resultSet.getInt("isUsed"));
            coupon.setUid(resultSet.getString("id"));
            
            resultList.add(coupon);
            */
            
            URI uri = uriInfo.getBaseUriBuilder()
                    .path(CouponsResource.class)
                    .path(CouponsResource.class, "getCoupon")
                    .build(resultSet.getString("id"));
            
            resultList.add(uri);
        }
        
        return resultList;
    }
    
    public ArrayList<URI> getCustomerCoupons(int customerUid, UriInfo uriInfo) throws SQLException {
        
        ArrayList<URI> resultList = new ArrayList<>();
        
        String sql = "SELECT id FROM coupons WHERE customerId = ?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setInt(1, customerUid);
        
        ResultSet resultSet = stmt.executeQuery();
        while (resultSet.next()) {
            /**
            Coupon coupon = new Coupon();
            coupon.setCustomerUid(resultSet.getInt("customerUid"));
            coupon.setExpirationDate(LocalDate.parse(resultSet.getDate("expirationDate").toString()));
            coupon.setPriceCut(resultSet.getInt("priceCut"));
            coupon.setIsUsed(resultSet.getInt("isUsed"));
            coupon.setUid(resultSet.getString("id"));
            
            resultList.add(coupon);
            */
            
            URI uri = uriInfo.getBaseUriBuilder()
                    .path(CouponsResource.class)
                    .path(CouponsResource.class, "getCoupon")
                    .build(resultSet.getString("id"));
            
            resultList.add(uri);
        }
        
        return resultList;
    }
    
}
