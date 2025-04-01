/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eat_it.restApp_Client;

import eat_it.model.Category;
import eat_it.model.Product;
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
public class ProductPathExecutor {
    
    private static final String baseURI = "http://localhost:8080/eatIt/rest";
    
    public static void doTests() {
        
        System.out.println("3) Product tests:\n");
        
        System.out.println("- get all products");
        
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
        WebTarget productTarget = singleCategoryTarget.path("products");
        
        Builder prodBuilder = baseTarget.path("products").request(MediaType.APPLICATION_JSON);
        Invocation invocation = prodBuilder.buildGet();
        runAndPrint(invocation, "GET", "http://localhost:8080" + productTarget.getUri().getPath(), "Retrieves the full product list", null, null);
        
        System.out.println();
        System.out.println("- get product");
        System.out.println();
        
        WebTarget singleProductTarget = productTarget.path("{ProdUID}").resolveTemplate("ProdUID", 2);
        prodBuilder = singleProductTarget.request(MediaType.APPLICATION_JSON);
        invocation = prodBuilder.buildGet();
        runAndPrint(invocation, "GET", "http://localhost:8080" + singleProductTarget.getUri().getPath(), "Retrieves a single product", null, null);
        
        System.out.println();
        System.out.println("- category product list");
        System.out.println();
        
        prodBuilder = singleCategoryTarget.request(MediaType.APPLICATION_JSON);
        invocation = prodBuilder.buildGet();
        runAndPrint(invocation, "GET", "http://localhost:8080" + singleCategoryTarget.getUri().getPath(), "Retrieves the product list of a certain category", null, null);
        
        System.out.println();
        System.out.println("- post product");
        System.out.println();
        
        Product product = new Product();
        product.setName("productName");
        product.setDescription("description");
        product.setPrice((float) 12.5);
        product.setImagePath("imagePath");
        
        WebTarget singleCategoryProductTarget = singleCategoryTarget.path("products");
        
        prodBuilder = singleCategoryProductTarget.request(MediaType.TEXT_PLAIN_TYPE).header(HttpHeaders.AUTHORIZATION, "Bearer " + adminAuthToken);
        invocation = prodBuilder.buildPost(Entity.json(product));
        runAndPrint(invocation, "POST", "http://localhost:8080" + singleCategoryProductTarget.getUri().getPath(), "Creates a new product of a certain category", Entity.json(product).toString(), MediaType.APPLICATION_JSON);
        //.header(HttpHeaders.AUTHORIZATION, "Bearer " + adminAuthToken)
        
        System.out.println();
        System.out.println("- modify product");
        System.out.println();
        
        product.setUid(54);
        prodBuilder = singleCategoryProductTarget.request().header(HttpHeaders.AUTHORIZATION, "Bearer " + adminAuthToken);
        invocation = prodBuilder.buildPut(Entity.json(product));
        runAndPrint(invocation, "PUT", "http://localhost:8080" + singleCategoryProductTarget.getUri().getPath(), "Updates a product", Entity.json(product).toString(), MediaType.APPLICATION_JSON);
        
        System.out.println();
        System.out.println("- delete product");
        System.out.println();
        
        singleProductTarget = productTarget.path("{ProdUID}").resolveTemplate("ProdUID", 54);
        prodBuilder = singleProductTarget.request().header(HttpHeaders.AUTHORIZATION, "Bearer " + adminAuthToken);
        invocation = prodBuilder.buildDelete();
        runAndPrint(invocation, "DELETE", "http://localhost:8080" + singleProductTarget.getUri().getPath(), "Deletes a product", null, null);
        
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
