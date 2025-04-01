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
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Form;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.Response;

/**
 *
 * @author anton
 */
public class StatisticsPathExecutor {
    
    private static final String baseURI = "http://localhost:8080/eatIt/rest";
    
    public static void doTests() {
        
        System.out.println("9) statistics tests:\n");
        
        System.out.println("- get statistics");
        
        Client client = ClientBuilder.newClient();
        
        WebTarget baseTarget = client.target(baseURI);
        WebTarget authTarget = baseTarget.path("auth");
        
        Form formAdmin = new Form().param("email", "eatIt@gmail.com").param("password", "password");
        
        //
        WebTarget loginTarget = authTarget.path("login");
        Response response = loginTarget.request().post(Entity.form(formAdmin));
        String adminAuthToken = response.readEntity(String.class);
        //
        
        WebTarget statisticsTarget = baseTarget.path("statistics");
        Invocation.Builder statBuilder = statisticsTarget.request(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, "Bearer " + adminAuthToken);
        Invocation invocation = statBuilder.buildGet();
        runAndPrint(invocation, "GET", "http://localhost:8080" + statisticsTarget.getUri().getPath(), "Retrieves the restaurant's current statistics for the month", null, null);
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
