/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eat_it.service;

import eat_it.connectionManager.DBConnectionManager;
import eat_it.model.Statistics;
import jakarta.ws.rs.core.UriInfo;
import java.net.URI;
import java.sql.Connection;
import java.util.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 *
 * @author anton
 */
public class StatisticsService {
    
    Connection connection = DBConnectionManager.getConnection();
    ProductsService productsService = new ProductsService();
    
    public Statistics getStatistics(UriInfo uriInfo) throws SQLException {
        
        Statistics statistics = new Statistics();
        
        ArrayList<URI> bestWorstList = productsService.getBestWorstProduct(uriInfo);
        statistics.setBestSeller(bestWorstList.get(0));
        statistics.setWorstSeller(bestWorstList.get(1));
        
        //Statement stmt = connection.createStatement();
        String sql = "select sum(total) from orders where orderState=\"Completed\" and extract(Month from creationDate) = ? and extract(Year from creationDate) = ?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int month = cal.get(Calendar.MONTH) + 1;
        int year = cal.get(Calendar.YEAR);
        
        stmt.setInt(1, month);
        stmt.setInt(2, year);
        ResultSet resultSet = stmt.executeQuery();
        //System.out.println(stmt.toString());

        while (resultSet.next()){
            statistics.setMontlyRevenues(resultSet.getInt("sum(total)"));
        }
        
        sql = "select count(*) as numorders, extract(Month from creationDate) as ordersMonth from orders where extract(Year from creationDate)= ? group by extract(Month from creationDate) order by extract(Month from creationDate)";
        stmt = connection.prepareStatement(sql);
        stmt.setInt(1, year);
        resultSet = stmt.executeQuery();
        //System.out.println(stmt.toString());
        
        Map<String, Integer> map = new HashMap<>();
        
        while (resultSet.next()) {
            DateFormatSymbols dfs = new DateFormatSymbols(Locale.ITALY);
            String mese = dfs.getMonths()[resultSet.getInt("ordersMonth")];
            map.put(mese, resultSet.getInt("numorders"));
        }
        
        statistics.setMonthlyOrders(map);
        
        return statistics;
    }
}
