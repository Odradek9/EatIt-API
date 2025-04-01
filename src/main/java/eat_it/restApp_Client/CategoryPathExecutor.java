/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eat_it.restApp_Client;

import eat_it.model.Category;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.Invocation.Builder;
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
public class CategoryPathExecutor {
    
    private static final String baseURI = "http://localhost:8080/eatIt/rest";
    //private CloseableHttpClient client = HttpClients.createDefault();
    
    public static void doTests() {
        
        System.out.println("2) Category tests:\n");
        
        System.out.println("- category info");
        
        Client client = ClientBuilder.newClient();
        
        WebTarget baseTarget = client.target(baseURI);
        WebTarget categoryTarget = baseTarget.path("categories");
        
        WebTarget singleCategoryTarget = categoryTarget.path("{CatUID}").resolveTemplate("CatUID", 2);
        Builder catBuilder = singleCategoryTarget.request(MediaType.APPLICATION_JSON);
        Invocation invocation = catBuilder.buildGet();
        runAndPrint(invocation, "GET", "http://localhost:8080" + singleCategoryTarget.getUri().getPath(), "Retrives a single category's info", null, null);
        
        System.out.println();
        System.out.println("- category list");
        System.out.println();
        
        catBuilder = categoryTarget.request(MediaType.APPLICATION_JSON);
        invocation = catBuilder.buildGet();
        runAndPrint(invocation, "GET", "http://localhost:8080" + categoryTarget.getUri().getPath(), "Retrives the full category URI list", null, null);
        
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
        
        System.out.println();
        System.out.println("- category best/worst sellers");
        System.out.println();
        
        WebTarget bestWorstCategoryTarget = categoryTarget.path("BestWorstSellers");
        catBuilder = bestWorstCategoryTarget.request(MediaType.TEXT_PLAIN_TYPE).header(HttpHeaders.AUTHORIZATION, "Bearer " + adminAuthToken);
        invocation = catBuilder.buildGet();
        runAndPrint(invocation, "GET", "http://localhost:8080" + bestWorstCategoryTarget.getUri().getPath(), "Retrieves a map containing the product info of the best and worst sellers (in order) for each category", null, null);
        
        System.out.println();
        System.out.println("- insert category");
        System.out.println();
        
        catBuilder = categoryTarget.request(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, "Bearer " + adminAuthToken);
        Category category = new Category();
        category.setCategoryName("CategoryName");
        category.setImagePath("ImagePath");
        invocation = catBuilder.buildPost(Entity.json(category));
        runAndPrint(invocation, "POST", "http://localhost:8080" + categoryTarget.getUri().getPath(), "Creates a new category", Entity.json(category).toString(), MediaType.APPLICATION_JSON);
        
        System.out.println();
        System.out.println("- modify category");
        System.out.println();
        
        category.setUid(8);
        invocation = catBuilder.buildPut(Entity.json(category));
        runAndPrint(invocation, "PUT", "http://localhost:8080" + categoryTarget.getUri().getPath(), "Updates a category", Entity.json(category).toString(), MediaType.APPLICATION_JSON);
        
        System.out.println();
        System.out.println("- delete category");
        System.out.println();
        
        WebTarget singleCategoryTarget_2 = categoryTarget.path("{CatUID}").resolveTemplate("CatUID", 11);
        catBuilder = singleCategoryTarget_2.request(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, "Bearer " + adminAuthToken);
        invocation = catBuilder.buildDelete();
        runAndPrint(invocation, "DELETE", "http://localhost:8080" + singleCategoryTarget_2.getUri().getPath(), "Deletes a category", null, null);
        
        /**
        HttpGet get_request = new HttpGet(baseURI + "/categories/");
        get_request.setHeader("Accept", "application/json");
        runAndPrint(get_request);

        System.out.println();
        */
        
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
