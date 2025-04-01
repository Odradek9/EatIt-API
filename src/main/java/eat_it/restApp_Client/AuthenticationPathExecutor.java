/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eat_it.restApp_Client;

import eat_it.model.Customer;
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

/**
 *
 * @author anton
 */
public class AuthenticationPathExecutor {

    private static final String baseURI = "http://localhost:8080/eatIt/rest";
    
    public static void doTests() {
        
        System.out.println("1) Authentication tests:\n");
        
        System.out.println();
        System.out.println("- login");
        System.out.println();
        
        Client client = ClientBuilder.newClient();
        
        WebTarget baseTarget = client.target(baseURI);
        WebTarget authTarget = baseTarget.path("auth");
        
        Form form = new Form().param("email", "eatIt@gmail.com").param("password", "password");
        
        WebTarget loginTarget = authTarget.path("login");
        Invocation invocation = loginTarget.request().buildPost(Entity.form(form));
        runAndPrint(invocation, "POST", "http://localhost:8080" + loginTarget.getUri().getPath(), "Performs the login by sending the username/password pair and receiving an authorization token", form.asMap().toString(), MediaType.APPLICATION_FORM_URLENCODED);
        
        //
        Response response = loginTarget.request().post(Entity.form(form));
        String adminAuthToken = response.readEntity(String.class);
        //
        
        System.out.println();
        System.out.println("- refresh");
        System.out.println();
        
        WebTarget refreshTarget = authTarget.path("refresh");
        invocation = refreshTarget.request(MediaType.TEXT_PLAIN).header(HttpHeaders.AUTHORIZATION, "Bearer " + adminAuthToken).buildGet();
        runAndPrint(invocation, "GET", "http://localhost:8080" + refreshTarget.getUri().getPath(), "Refreshes the current authorization token", null, null);
        
        System.out.println();
        System.out.println("- logout");
        System.out.println();
        
        WebTarget logoutTarget = authTarget.path("logout");
        invocation = logoutTarget.request().header(HttpHeaders.AUTHORIZATION, "Bearer " + adminAuthToken).buildDelete();
        runAndPrint(invocation, "DELETE", "http://localhost:8080" + logoutTarget.getUri().getPath(), "Performs the logout", null, null);
        
        System.out.println();
        System.out.println("- signup");
        System.out.println();
        
        WebTarget signupTarget = authTarget.path("signup");
        Customer customer = new Customer();
        customer.setName("customerName");
        customer.setSurname("customerSurname");
        customer.setEmail("test@gmail.com");
        customer.setPassword("password");
        invocation = signupTarget.request().buildPost(Entity.json(customer));
        runAndPrint(invocation, "POST", "http://localhost:8080" + signupTarget.getUri().getPath(), "Creates a new customer", Entity.json(customer).toString(), MediaType.APPLICATION_JSON);
        
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
        if (entity != null) {
            System.out.println();
            System.out.println("Result entity: ");
            System.out.println(entity);
            System.out.println();
        }
            
    }
}
