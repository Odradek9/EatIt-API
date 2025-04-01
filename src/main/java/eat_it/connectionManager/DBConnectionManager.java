/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eat_it.connectionManager;

/**
 *
 * @author anton
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class DBConnectionManager {
    
    private static Connection connection;
    private static String dbUrl = "jdbc:mysql://localhost:3306/tasteit?zeroDateTimeBehavior=CONVERT_TO_NULL";
    private static String user = "root";
    private static String password = "password";
    
    public static Connection getConnection(){
        
        
        if (connection == null){
            try{
                DriverManager.registerDriver(new com.mysql.jdbc.Driver ());
                connection = (Connection) DriverManager.getConnection(dbUrl, user, password);
                System.out.println("Connected to MySQL database");
            }
            catch(SQLException e){
                System.out.println(e.getMessage());
            }
        }
        return connection;
    }

}