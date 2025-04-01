/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eat_it.service;

import eat_it.connectionManager.DBConnectionManager;
import eat_it.model.Address;
import eat_it.resource.AddressesResource;
import eat_it.resource.CustomersResource;
import eat_it.restApp.DatabaseException;
import jakarta.ws.rs.core.Context;
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
public class AddressesService {
    
    Connection connection = DBConnectionManager.getConnection();

    public int createNewCustomerAddress(int customerUid, Address address) throws SQLException, DatabaseException {
        
        int resultUid = 0;
        
        String sql = "INSERT INTO shippingaddresses (`cap`, `city`, `street`, `homeNumber`, `customerid`) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        stmt.setInt(1, address.getCap());
        stmt.setString(2, address.getCity());
        stmt.setString(3, address.getStreet());
        stmt.setInt(4, address.getHomeNumber());
        stmt.setInt(5, customerUid);
        
        int addedRows = stmt.executeUpdate();
        
        if (addedRows == 0) {
            throw new DatabaseException("Failed to create new item. SQL request was: " + stmt.toString());
        }
        ResultSet keys = stmt.getGeneratedKeys();
        while (keys.next()) {
            resultUid = keys.getInt(1);
        }
        
        return resultUid;
    }

    public Address getCustomerAddress(int customerUid, int addressUid) throws SQLException {
        
        Address address = new Address();        
        
        String sql = "select * from shippingaddresses where customerId = ? AND id = ?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setInt(1, customerUid);
        stmt.setInt(2, addressUid);
        ResultSet resultSet = stmt.executeQuery();
        while (resultSet.next()){
            
            address.setUid(resultSet.getInt("id"));
            address.setCap(resultSet.getInt("cap"));
            address.setCity(resultSet.getString("city"));
            address.setStreet(resultSet.getString("street"));
            address.setHomeNumber(resultSet.getInt("homeNumber"));
            
        }
        
        return address;
    }
    
    public ArrayList<URI> getCustomerAddresses(int customerUid, @Context UriInfo uriInfo) throws SQLException {
        
        ArrayList<URI> resultList = new ArrayList<>();
        
        String sql = "select id from shippingaddresses where customerId = ?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setInt(1, customerUid);
        ResultSet resultSet = stmt.executeQuery();
        while (resultSet.next()){
            /**
            address.setUid(resultSet.getInt("id"));
            address.setCap(resultSet.getInt("cap"));
            address.setCity(resultSet.getString("city"));
            address.setStreet(resultSet.getString("street"));
            address.setHomeNumber(resultSet.getInt("homeNumber"));
            */
            
            URI uri = uriInfo.getBaseUriBuilder()
                    .path(CustomersResource.class)
                    .path(CustomersResource.class, "getAddressesResource")
                    .path(AddressesResource.class, "getAddress")
                    .build(customerUid, resultSet.getInt("id"));
            
            resultList.add(uri);
        }
        
        return resultList;
    }
    
}
