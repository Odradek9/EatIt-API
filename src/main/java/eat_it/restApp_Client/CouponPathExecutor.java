/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eat_it.restApp_Client;

import eat_it.model.Coupon;
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
import java.time.LocalDate;

/**
 *
 * @author anton
 */
public class CouponPathExecutor {
    
    private static final String baseURI = "http://localhost:8080/eatIt/rest";
    
    public static void doTests() {
        
        System.out.println("6) coupon tests:\n");
        
        System.out.println("- coupon list (customer)");
        
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
        
        WebTarget couponTarget = baseTarget.path("coupons");
        
        Invocation.Builder coupBuilder = couponTarget.request(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, "Bearer " + customerAuthToken);
        Invocation invocation = coupBuilder.buildGet();
        runAndPrint(invocation, "GET", "http://localhost:8080" + couponTarget.getUri().getPath(), "Retrieves a coupon URI list, depending on authorization", null, null);
        
        System.out.println();
        System.out.println("- coupon list (admin)");
        System.out.println();
        
        coupBuilder = couponTarget.request(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, "Bearer " + adminAuthToken);
        invocation = coupBuilder.buildGet();
        runAndPrint(invocation, "GET", "http://localhost:8080" + couponTarget.getUri().getPath(), "Retrieves a coupon URI list, depending on authorization", null, null);
        
        System.out.println();
        System.out.println("- post coupon");
        System.out.println();
        
        Coupon coupon = new Coupon();
        
        coupon.setExpirationDate(LocalDate.now().plusMonths(2));
        coupon.setPriceCut(25);
        coupon.setCustomerUid(7);
        coupBuilder = couponTarget.request(MediaType.TEXT_PLAIN).header(HttpHeaders.AUTHORIZATION, "Bearer " + adminAuthToken);
        invocation = coupBuilder.buildPost(Entity.json(coupon));
        runAndPrint(invocation, "POST", "http://localhost:8080" + couponTarget.getUri().getPath(), "Creates a new coupon", Entity.json(coupon).toString(), MediaType.APPLICATION_JSON);
        
        System.out.println();
        System.out.println("- coupon info (customer)");
        System.out.println();
        
        WebTarget singleCouponTarget = couponTarget.path("{CoupUID}").resolveTemplate("CoupUID", "C62c55f02f378f");
        coupBuilder = singleCouponTarget.request(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, "Bearer " + customerAuthToken);
        invocation = coupBuilder.buildGet();
        runAndPrint(invocation, "GET", "http://localhost:8080" + singleCouponTarget.getUri().getPath(), "Retrieves a single coupon", null, null);
        
        System.out.println();
        System.out.println("- coupon info (admin)");
        System.out.println();
        
        coupBuilder = singleCouponTarget.request(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, "Bearer " + adminAuthToken);
        invocation = coupBuilder.buildGet();
        runAndPrint(invocation, "GET", "http://localhost:8080" + singleCouponTarget.getUri().getPath(), "Retrieves a single coupon", null, null);
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
