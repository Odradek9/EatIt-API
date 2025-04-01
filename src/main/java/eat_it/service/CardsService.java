/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eat_it.service;

import eat_it.connectionManager.DBConnectionManager;
import eat_it.model.CreditCard;
import eat_it.resource.CardsResource;
import eat_it.resource.CustomersResource;
import eat_it.restApp.DatabaseException;
import jakarta.ws.rs.core.UriInfo;
import java.net.URI;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 *
 * @author anton
 */
public class CardsService {
    
    Connection connection = DBConnectionManager.getConnection();

    public CreditCard getCustomerCard(int customerUid, int cardUid) throws SQLException {
        
        CreditCard card = new CreditCard();        
        
        String sql = "select * from customers_paymentmethods where customerId = ? AND id = ?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setInt(1, customerUid);
        stmt.setInt(2, cardUid);
        ResultSet resultSet = stmt.executeQuery();
        while (resultSet.next()){
            
            card.setUid(resultSet.getInt("id"));
            card.setCardHolder(resultSet.getString("cardHolder"));
            card.setNumber(resultSet.getInt("cardNumber"));
            card.setCvv(resultSet.getInt("cvv"));
            card.setExpirationDate(LocalDate.parse(resultSet.getString("expirationDate")));
        }
        return card;
    }
    
    public ArrayList<URI> getCustomerCards(int customerUid, UriInfo uriInfo) throws SQLException {
        
        ArrayList<URI> resultList = new ArrayList<>();
        
        String sql = "select id from customers_paymentmethods where customerId = ?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setInt(1, customerUid);
        ResultSet resultSet = stmt.executeQuery();
        while (resultSet.next()){
            /**
            card.setUid(resultSet.getInt("id"));
            card.setCardHolder(resultSet.getString("cardHolder"));
            card.setNumber(resultSet.getInt("cardNumber"));
            card.setCvv(resultSet.getInt("cvv"));
            card.setExpirationDate(LocalDate.parse(resultSet.getString("expirationDate")));
            */
            URI uri = uriInfo.getBaseUriBuilder()
                    .path(CustomersResource.class)
                    .path(CustomersResource.class, "getCardsResource")
                    .path(CardsResource.class, "getCustomerCard")
                    .build(customerUid, resultSet.getInt("id"));
            
            resultList.add(uri);
        }
        return resultList;
    }
    
    public int createNewCustomerCard(int customerUid, CreditCard card) throws SQLException, DatabaseException {
    
        int resultUid = 0;
        
        String sql = "INSERT INTO customers_paymentmethods (`customerId`, `cardNumber`, `expirationDate`, `cvv`, `cardHolder`) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        stmt.setInt(1, customerUid);
        stmt.setInt(2, card.getNumber());
        stmt.setDate(3, Date.valueOf(card.getExpirationDate()));
        stmt.setInt(4, card.getCvv());
        stmt.setString(5, card.getCardHolder());
        
        int addedRows = stmt.executeUpdate();
        
        if(addedRows == 0) {
            throw new DatabaseException("Failed to create new item. SQL request was: " + stmt.toString());
        }
        
        ResultSet keys = stmt.getGeneratedKeys();
        while (keys.next()) {
            resultUid = keys.getInt(1);
        }
        
        return resultUid;
}
    
}
