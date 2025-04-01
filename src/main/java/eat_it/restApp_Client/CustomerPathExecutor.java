/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eat_it.restApp_Client;

import eat_it.model.Address;
import eat_it.model.CreditCard;
import eat_it.model.JsonData;
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
import java.time.LocalDate;
import java.time.ZoneId;
import javax.smartcardio.Card;
import static org.graalvm.compiler.nodes.memory.address.OffsetAddressNode.address;

/**
 *
 * @author anton
 */
public class CustomerPathExecutor {
    
    private static final String baseURI = "http://localhost:8080/eatIt/rest";
    
    public static void doTests() {
        
        System.out.println("5) customer tests:\n");
        
        System.out.println("- best customer list");
        
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
        
        WebTarget customerTarget = baseTarget.path("customers");
        WebTarget bestCustomerTarget = customerTarget.path("best");
        Builder customerBuilder = bestCustomerTarget.request(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, "Bearer " + adminAuthToken);
        Invocation invocation = customerBuilder.buildGet();
        runAndPrint(invocation, "GET", "http://localhost:8080" + bestCustomerTarget.getUri().getPath(), "Retrives a list of ten customers ranked by total spending", null, null);
        
        System.out.println();
        System.out.println("- full customer list");
        System.out.println();
        
        customerBuilder = customerTarget.request(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, "Bearer " + adminAuthToken);
        invocation = customerBuilder.buildGet();
        runAndPrint(invocation, "GET", "http://localhost:8080" + customerTarget.getUri().getPath(), "Retrives a full list of customers", null, null);
        
        System.out.println();
        System.out.println("- customer addresses");
        System.out.println();
        
        WebTarget singleCustomerTarget = customerTarget.path("{CustomerUID}").resolveTemplate("CustomerUID", 7);
        
        WebTarget addressTarget = singleCustomerTarget.path("addresses");
        customerBuilder = addressTarget.request(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, "Bearer " + customerAuthToken);
        invocation = customerBuilder.buildGet();
        runAndPrint(invocation, "GET", "http://localhost:8080" + addressTarget.getUri().getPath(), "Retrieves all customer addresses", null, null);
        
        System.out.println();
        System.out.println("- customer address info");
        System.out.println();
        
        WebTarget singleAddressTarget = addressTarget.path("{AddUID}").resolveTemplate("AddUID", 6);
        customerBuilder = singleAddressTarget.request(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, "Bearer " + customerAuthToken);
        invocation = customerBuilder.buildGet();
        runAndPrint(invocation, "GET", "http://localhost:8080" + singleAddressTarget.getUri().getPath(), "Retrieves all customer addresses", null, null);
        
        System.out.println();
        System.out.println("- post customer address");
        System.out.println();
        
        Address address = new Address();
        address.setCap(67039);
        address.setCity("Sulmona");
        address.setStreet("viale togliatti");
        address.setHomeNumber(2);
        customerBuilder = addressTarget.request(MediaType.TEXT_PLAIN).header(HttpHeaders.AUTHORIZATION, "Bearer " + customerAuthToken);
        invocation = customerBuilder.buildPost(Entity.json(address));
        runAndPrint(invocation, "POST", "3http://localhost:8080" + addressTarget.getUri().getPath(), "Creates a new address of a specific customer", Entity.json(address).toString(), MediaType.APPLICATION_JSON);
        
        System.out.println();
        System.out.println("- customer card info");
        System.out.println();
        
        WebTarget cardTarget = singleCustomerTarget.path("cards");
        
        WebTarget singleCardTarget = cardTarget.path("{CardUID}").resolveTemplate("CardUID", 5);
        customerBuilder = singleCardTarget.request(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, "Bearer " + customerAuthToken);
        invocation = customerBuilder.buildGet();
        runAndPrint(invocation, "GET", "http://localhost:8080" + singleCardTarget.getUri().getPath(), "Retrieves a single customer's card", null, null);
        
        System.out.println();
        System.out.println("- post customer card");
        System.out.println();
        
        CreditCard card = new CreditCard();
        card.setCardHolder("CardHolder");
        card.setCvv(1111);
        card.setExpirationDate(LocalDate.now(ZoneId.systemDefault()));
        card.setNumber(1212121212);
        customerBuilder = cardTarget.request(MediaType.TEXT_PLAIN).header(HttpHeaders.AUTHORIZATION, "Bearer " + customerAuthToken);
        invocation = customerBuilder.buildPost(Entity.json(card));
        runAndPrint(invocation, "POST", "http://localhost:8080" + cardTarget.getUri().getPath(), "Creates a new card of a specific customer", Entity.json(card).toString(), MediaType.APPLICATION_JSON);
        
        System.out.println();
        System.out.println("- customer cards");
        System.out.println();
        
        customerBuilder = cardTarget.request(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, "Bearer " + customerAuthToken);
        invocation = customerBuilder.buildGet();
        runAndPrint(invocation, "GET", "http://localhost:8080" + cardTarget.getUri().getPath(), "Retrieves all of a customer's card", null, null);
        
        System.out.println();
        System.out.println("- customer favourites");
        System.out.println();
        
        WebTarget favouritesTarget = singleCustomerTarget.path("favourites");
        customerBuilder = favouritesTarget.request(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, "Bearer " + customerAuthToken);
        invocation = customerBuilder.buildGet();
        runAndPrint(invocation, "GET", "http://localhost:8080" + favouritesTarget.getUri().getPath(), "Retrives a full list of a customer's favourite products", null, null);
        
        System.out.println();
        System.out.println("- post product to customer favourites");
        System.out.println();
        
        customerBuilder = favouritesTarget.request().header(HttpHeaders.AUTHORIZATION, "Bearer " + customerAuthToken);
        invocation = customerBuilder.buildPost(Entity.text(3));
        runAndPrint(invocation, "POST", "http://localhost:8080" + favouritesTarget.getUri().getPath(), "Adds a single product into the customer's favourites list", Entity.text(3).toString(), MediaType.TEXT_PLAIN);
        
        System.out.println();
        System.out.println("- delete product from customer favourites");
        System.out.println();
        
        WebTarget singleProductFavouritesTarget = favouritesTarget.path("{ProdUID}").resolveTemplate("ProdUID", 3);
        customerBuilder = singleProductFavouritesTarget.request().header(HttpHeaders.AUTHORIZATION, "Bearer " + customerAuthToken);
        invocation = customerBuilder.buildDelete();
        runAndPrint(invocation, "DELETE", "http://localhost:8080" + singleProductFavouritesTarget.getUri().getPath(), "Removes a single product from the customer's favourites list", null, null);
        
        System.out.println();
        System.out.println("- customer cart");
        System.out.println();
        
        WebTarget cartTarget = singleCustomerTarget.path("cart");
        customerBuilder = cartTarget.request(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, "Bearer " + customerAuthToken);
        invocation = customerBuilder.buildGet();
        runAndPrint(invocation, "GET", "http://localhost:8080" + cartTarget.getUri().getPath(), "Retrives a full list of products from a customer's cart", null, null);
        
        System.out.println();
        System.out.println("- post product to customer cart");
        System.out.println();
        
        JsonData jsonData = new JsonData();
        jsonData.setProductUid(2);
        jsonData.setQuantity(23);
        customerBuilder = cartTarget.request().header(HttpHeaders.AUTHORIZATION, "Bearer " + customerAuthToken);
        invocation = customerBuilder.buildPost(Entity.json(jsonData));
        runAndPrint(invocation, "POST", "http://localhost:8080" + cartTarget.getUri().getPath(), "Adds a number of a product into the customer's cart", Entity.json(jsonData).toString(), MediaType.APPLICATION_JSON);
        
        System.out.println();
        System.out.println("- put product to customer cart");
        System.out.println();
        
        customerBuilder = cartTarget.request().header(HttpHeaders.AUTHORIZATION, "Bearer " + customerAuthToken);
        invocation = customerBuilder.buildPut(Entity.json(jsonData));
        runAndPrint(invocation, "PUT", "http://localhost:8080" + cartTarget.getUri().getPath(), "Updates the quantity of a product in the customer's cart", Entity.json(jsonData).toString(), MediaType.APPLICATION_JSON);
        
        System.out.println();
        System.out.println("- delete product from customer cart");
        System.out.println();
        
        WebTarget singleProductCartTarget = cartTarget.path("{ProdUID}").resolveTemplate("ProdUID", 2);
        customerBuilder = singleProductCartTarget.request().header(HttpHeaders.AUTHORIZATION, "Bearer " + customerAuthToken);
        invocation = customerBuilder.buildDelete();
        runAndPrint(invocation, "DELETE", "http://localhost:8080" + singleProductCartTarget.getUri().getPath(), "Removes a product from the customer's Cart", null, null);
        
        System.out.println();
        System.out.println("- insert customer cart products into cart");
        System.out.println();
        
        WebTarget favCartTarget = singleCustomerTarget.path("favouritesToCart");
        customerBuilder = favCartTarget.request().header(HttpHeaders.AUTHORIZATION, "Bearer " + customerAuthToken);
        invocation = customerBuilder.buildPost(Entity.json(""));
        runAndPrint(invocation, "POST", "http://localhost:8080" + favCartTarget.getUri().getPath(), "Adds the products found in the customer's favourite list into the customer's cart", null, null);
        
        System.out.println();
        System.out.println("- customer cart checkout");
        System.out.println();
        
        JsonData jsonData_2 = new JsonData();
        jsonData_2.setAddressUid(6);
        customerBuilder = cartTarget.path("checkout").request().header(HttpHeaders.AUTHORIZATION, "Bearer " + customerAuthToken);
        invocation = customerBuilder.buildPost(Entity.json(jsonData_2));
        runAndPrint(invocation, "POST", "http://localhost:8080" + cartTarget.path("checkout").getUri().getPath(), "Adds the products found in the customer's favourite list into the customer's cart", Entity.json(jsonData_2).toString(), MediaType.APPLICATION_JSON);
        
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
