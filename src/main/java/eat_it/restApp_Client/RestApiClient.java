/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eat_it.restApp_Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

/**
 *
 * @author anton
 */
public class RestApiClient {
    
    public static void main(String[] arg) throws IOException {
        
        RestApiClient restClient = new RestApiClient();
        restClient.runTests();
        
    }
    
    public void runTests() {
        
        System.out.println("--------------------------------------------------------------------------------");
        System.out.println("--------------------------------------------------------------------------------");
        System.out.println();
        AuthenticationPathExecutor.doTests();
        System.out.println("--------------------------------------------------------------------------------");
        System.out.println("--------------------------------------------------------------------------------");
        System.out.println();
        CategoryPathExecutor.doTests();
        System.out.println("--------------------------------------------------------------------------------");
        System.out.println("--------------------------------------------------------------------------------");
        System.out.println();
        ProductPathExecutor.doTests();
        System.out.println("--------------------------------------------------------------------------------");
        System.out.println("--------------------------------------------------------------------------------");
        System.out.println();
        ReviewPathExecutor.doTests();
        System.out.println("--------------------------------------------------------------------------------");
        System.out.println("--------------------------------------------------------------------------------");
        System.out.println();
        CustomerPathExecutor.doTests();
        System.out.println("--------------------------------------------------------------------------------");
        System.out.println("--------------------------------------------------------------------------------");
        System.out.println();
        CouponPathExecutor.doTests();
        System.out.println("--------------------------------------------------------------------------------");
        System.out.println("--------------------------------------------------------------------------------");
        System.out.println();
        OrderPathExecutor.doTests();
        System.out.println("--------------------------------------------------------------------------------");
        System.out.println("--------------------------------------------------------------------------------");
        System.out.println();
        RestaurantPathExecutor.doTests();
        System.out.println("--------------------------------------------------------------------------------");
        System.out.println("--------------------------------------------------------------------------------");
        System.out.println();
        StatisticsPathExecutor.doTests();
    }
    
}
