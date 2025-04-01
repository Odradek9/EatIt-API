/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eat_it.service;

import eat_it.connectionManager.DBConnectionManager;
import eat_it.model.Address;
import eat_it.model.Restaurant;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author anton
 */
public class RestaurantService {
    
    Connection connection = DBConnectionManager.getConnection();
    
    public Restaurant getContacts() throws SQLException {
        
        Restaurant restaurant = new Restaurant();
        
        String sql = "SELECT r.email, r.name, r.phone, sa.cap, sa.city, sa.street, sa.homeNumber FROM restaurant AS r JOIN shippingaddresses AS sa on sa.id = r.addressId";
        PreparedStatement stmt = connection.prepareStatement(sql);
        ResultSet resultSet = stmt.executeQuery();

        while (resultSet.next()) {
            restaurant.setName(resultSet.getString("name"));
            restaurant.setEmail(resultSet.getString("email"));
            restaurant.setPhone(resultSet.getBigDecimal("phone"));
            Address address = new Address();
            address.setCap(resultSet.getInt("cap"));
            address.setCity(resultSet.getString("city"));
            address.setHomeNumber(resultSet.getInt("homeNumber"));
            address.setStreet(resultSet.getString("street"));
            restaurant.addAddress(address);
        }

        return restaurant;
    }
}
