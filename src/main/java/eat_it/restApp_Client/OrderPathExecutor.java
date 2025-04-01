/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eat_it.restApp_Client;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.client.Invocation.Builder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Form;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.Response;
import java.sql.Date;
import java.time.LocalDate;

/**
 *
 * @author anton
 */
public class OrderPathExecutor {
    
    private static final String baseURI = "http://localhost:8080/eatIt/rest";
    
    public static void doTests() {
        
        System.out.println("7) order tests:\n");
        
        System.out.println("- order list");
        
        Client client = ClientBuilder.newClient();
        
        WebTarget baseTarget = client.target(baseURI);
        WebTarget authTarget = baseTarget.path("auth");
        
        Form formAdmin = new Form().param("email", "eatIt@gmail.com").param("password", "password");
        Form formCustomer = new Form().param("email", "antonio_ranalli@outlook.it").param("password", "Odradek");
        
        //
        WebTarget loginTarget = authTarget.path("login");
        Response response = loginTarget.request().post(Entity.form(formAdmin));
        String adminAuthToken = response.readEntity(String.class);

        response = loginTarget.request().post(Entity.form(formCustomer));
        String customerAuthToken = response.readEntity(String.class);
        //
        
        WebTarget orderTarget = baseTarget.path("orders");
        Builder orderBuilder = orderTarget.request(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, "Bearer " + adminAuthToken);
        Invocation invocation = orderBuilder.buildGet();
        runAndPrint(invocation, "GET", "http://localhost:8080" + orderTarget.getUri().getPath(), "Retrieves a full orders URL list", null, null);
        
        System.out.println();
        System.out.println("- get order");
        System.out.println();
        
        WebTarget singleOrderTarget = orderTarget.path("{OrderUID}").resolveTemplate("OrderUID", 4);
        orderBuilder = singleOrderTarget.request(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, "Bearer " + adminAuthToken);
        invocation = orderBuilder.buildGet();
        runAndPrint(invocation, "GET", "http://localhost:8080" + singleOrderTarget.getUri().getPath(), "Retrieves a single order", null, null);
        
        System.out.println();
        System.out.println("- get order products");
        System.out.println();
        
        orderBuilder = singleOrderTarget.path("products").request(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, "Bearer " + adminAuthToken);
        invocation = orderBuilder.buildGet();
        runAndPrint(invocation, "GET", "http://localhost:8080" + singleOrderTarget.path("products").getUri().getPath(), "Retrieves a full list of an order's products", null, null);
        
        System.out.println();
        System.out.println("- refuse order");
        System.out.println();
        
        orderBuilder = singleOrderTarget.path("refuse").request().header(HttpHeaders.AUTHORIZATION, "Bearer " + adminAuthToken);
        invocation = orderBuilder.buildPost(null);
        runAndPrint(invocation, "POST", "http://localhost:8080" + singleOrderTarget.path("refuse").getUri().getPath(), "Changes the state of a single order to \"Denied\"", null, null); 
        
        System.out.println();
        System.out.println("- accept order");
        System.out.println();
        
        orderBuilder = singleOrderTarget.path("accept").request().header(HttpHeaders.AUTHORIZATION, "Bearer " + adminAuthToken);
        invocation = orderBuilder.buildPost(Entity.text("12:25"));
        runAndPrint(invocation, "POST", "http://localhost:8080" + singleOrderTarget.path("accept").getUri().getPath(), "Changes the state of a single order to \"Accepted\"", Entity.text("12:25").toString(), MediaType.TEXT_PLAIN); 
        
        System.out.println();
        System.out.println("- confirm order");
        System.out.println();
        
        orderBuilder = singleOrderTarget.path("confirm").request().header(HttpHeaders.AUTHORIZATION, "Bearer " + customerAuthToken);
        invocation = orderBuilder.buildPost(null);
        runAndPrint(invocation, "POST", "http://localhost:8080" + singleOrderTarget.path("confirm").getUri().getPath(), "Changes the state of a single order to \"Completed\"", null, null); 
        
        System.out.println();
        System.out.println("- order to cart");
        System.out.println();
        
        orderBuilder = singleOrderTarget.path("orderToCart").request().header(HttpHeaders.AUTHORIZATION, "Bearer " + customerAuthToken);
        invocation = orderBuilder.buildPost(null);
        runAndPrint(invocation, "POST", "http://localhost:8080" + singleOrderTarget.path("orderToCart").getUri().getPath(), "Copies a single's order contents into a customer's cart", null, null); 
    }
    
    private static void runAndPrint(Invocation invocation, String method, String url, String description, String requestEntity, String contentType) {
        
        System.out.println("--------------------------------------------------------------------------------");
        System.out.println(description);
        System.out.println("--------------------------------------------------------------------------------");
        System.out.println("REQUEST: ");
        System.out.println("* Method: " + method);
        System.out.println("* URL: " + url);
        
        switch (method) {
            case "POST": {
                if (requestEntity != null) {
                    System.out.print("* Payload: ");
                    System.out.println(requestEntity);
                }
                if (contentType != null) {
                    System.out.println("* Payload type: " + contentType);
                }
                break;
            }
            case "PUT": {
                if (requestEntity != null) {
                    System.out.print("* Payload: ");
                    System.out.println(requestEntity);
                }
                if (contentType != null) {
                    System.out.println("* Payload type: " + contentType);
                }
                break;
            }
            default:
                break;
        }
        Response response = invocation.invoke();
            
        System.out.println("RESPONSE: ");
        System.out.println("* Headers: ");
        MultivaluedMap<String, Object> headers = response.getHeaders();
        for (String header : headers.keySet()) {
            System.out.println("** " + header + " = " + headers.get(header));
        }
        System.out.println("* Return status: " + response.getStatusInfo().getReasonPhrase() + " (" + response.getStatus() + ")");
        String entity = response.readEntity(String.class);
        //Object entity = response.getEntity();
        if (entity != null && !entity.isEmpty()) {
            System.out.println();
            System.out.println("Result entity: ");
            System.out.println(entity);
            System.out.println();
        }
            
    }
}
