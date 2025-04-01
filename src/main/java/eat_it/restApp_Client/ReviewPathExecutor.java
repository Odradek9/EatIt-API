/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eat_it.restApp_Client;

import eat_it.model.Review;
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
public class ReviewPathExecutor {
    
    private static final String baseURI = "http://localhost:8080/eatIt/rest";
    
    public static void doTests() {
        
        System.out.println("4) review tests:\n");
        
        System.out.println("- product review list");
        
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
        
        WebTarget categoryTarget = baseTarget.path("categories");
        WebTarget singleCategoryTarget = categoryTarget.path("{CatUID}").resolveTemplate("CatUID", 2);
        WebTarget productTarget = singleCategoryTarget.path("products").path("{ProdUID}").resolveTemplate("ProdUID", 2);
        
        WebTarget reviewTarget = productTarget.path("reviews");
        Builder prodBuilder = reviewTarget.request(MediaType.APPLICATION_JSON);
        Invocation invocation = prodBuilder.buildGet();
        runAndPrint(invocation, "GET", "http://localhost:8080" + reviewTarget.getUri().getPath(), "Retrieves a product's review URI list", null, null);
        
        System.out.println();
        System.out.println("- product review info");
        System.out.println();
        
        WebTarget singleReviewTarget = reviewTarget.path("{RevUID}").resolveTemplate("RevUID", 3);
        prodBuilder = singleReviewTarget.request(MediaType.APPLICATION_JSON);
        invocation = prodBuilder.buildGet();
        runAndPrint(invocation, "GET", "http://localhost:8080" + singleReviewTarget.getUri().getPath(), "Retrieves a single product's review", null, null);
        
        System.out.println();
        System.out.println("- post product review");
        System.out.println();
        
        Review review = new Review();
        review.setStars(3);
        review.setCustomerUid(7);
        review.setComment("comment");
        prodBuilder = reviewTarget.request(MediaType.TEXT_PLAIN_TYPE).header(HttpHeaders.AUTHORIZATION, "Bearer " + customerAuthToken);
        invocation = prodBuilder.buildPost(Entity.json(review));
        runAndPrint(invocation, "POST", "http://localhost:8080" + singleReviewTarget.getUri().getPath(), "Creates a new review of a certain product", Entity.json(review).toString(), MediaType.APPLICATION_JSON);
        
        System.out.println();
        System.out.println("- delete product review");
        System.out.println();
        
        WebTarget singleReviewTarget_2 = reviewTarget.path("{RevUID}").resolveTemplate("RevUID", 17);
        prodBuilder = singleReviewTarget_2.request(MediaType.TEXT_PLAIN_TYPE).header(HttpHeaders.AUTHORIZATION, "Bearer " + customerAuthToken);
        invocation = prodBuilder.buildDelete();
        runAndPrint(invocation, "DELETE", "http://localhost:8080" + singleReviewTarget_2.getUri().getPath(), "Deletes a productReview", null, null);
        
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
