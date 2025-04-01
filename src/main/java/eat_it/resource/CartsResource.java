/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eat_it.resource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import eat_it.jackson.ObjectMapperContextResolver;
import eat_it.model.JsonData;
import eat_it.model.JsonList;
import eat_it.security.Logged;
import eat_it.service.CartsService;
import eat_it.service.OrdersService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import java.net.URI;
import java.sql.SQLException;

/**
 *
 * @author anton
 */
public class CartsResource {
    
    CartsService cartsService = new CartsService();
    OrdersService ordersService = new OrdersService();
    
    @GET
    @Logged
    @RolesAllowed({"CUSTOMER", "ADMIN"})
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCart(@PathParam("CustomerUID") int customerUid, @Context ContainerRequestContext req, @Context UriInfo uriInfo) {
        
        if (!req.getProperty("id").equals(customerUid)) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        
        try {
            //Get custom serializer to turn the productUID into URLs
            JsonList result = cartsService.getCustomerCart(customerUid, uriInfo);
            if (result.getList().isEmpty()) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            else {
                ObjectMapperContextResolver o = new ObjectMapperContextResolver();
                ObjectMapper objectMapper = o.getContext(JsonList.class);
                return Response.ok(objectMapper.writeValueAsString(result)).build();
            }
        } catch (SQLException ex) {
            System.out.println(ex);
            return Response.serverError().build();
        } catch (JsonProcessingException ex) {
            return Response.serverError().build();
        }
    }
    
    @POST
    @Logged
    @RolesAllowed({"CUSTOMER"})
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addProductToCustomerCart(@PathParam("CustomerUID") int customerUid, JsonData data, @Context ContainerRequestContext req) {
        
        if (!req.getProperty("id").equals(customerUid)) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        
        try {
            boolean preCheck = cartsService.isProductInCart(data.getProductUid(), customerUid);
            if (preCheck) {
                return Response.status(Response.Status.FORBIDDEN).build();
            }
            boolean check = cartsService.addProductToCustomerCart(customerUid, data.getProductUid(), data.getQuantity());
            if (check == false) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            else {
                return Response.noContent().build();
            }
        } catch (SQLException ex) {
            System.out.println(ex);
            return Response.serverError().build();
        }
    }
    
    @PUT
    @Logged
    @RolesAllowed({"CUSTOMER"})
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateProductInCustomerCart(@PathParam("CustomerUID") int customerUid, JsonData data, @Context ContainerRequestContext req) {
        
        if (!req.getProperty("id").equals(customerUid)) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        
        try {
            boolean check = cartsService.updateProductInCustomerCart(customerUid, data.getProductUid(), data.getQuantity());
            if (check == false) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            else {
                return Response.noContent().build();
            }
        } catch (SQLException ex) {
            System.out.println(ex);
            return Response.serverError().build();
        }
    }
    
    @Path("/{ProdUID}")
    @DELETE
    @Logged
    @RolesAllowed({"CUSTOMER"})
    public Response deleteProductFromCustomerCart(@PathParam("ProdUID") int prodUid, @PathParam("CustomerUID") int customerUid, @Context ContainerRequestContext req) {
        
        if (!req.getProperty("id").equals(customerUid)) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        
        try {
            boolean check = cartsService.deleteProductFromCart(customerUid, prodUid);
            if (check == false) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            else {
                return Response.noContent().build();
            }
        } catch (SQLException ex) {
            System.out.println(ex);
            return Response.serverError().build();
        }
        
    }
    
    @Path("/checkout")
    @POST
    @Logged
    @RolesAllowed({"CUSTOMER"})
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createOrderFromCart(@PathParam("CustomerUID") int customerUid, JsonData data, @Context UriInfo uriInfo, @Context ContainerRequestContext req) {
        
        if (!req.getProperty("id").equals(customerUid)) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        
        URI uri;
        try {
            float total = cartsService.getCartTotal(customerUid);
            if (total == 0) {
                return Response.ok("Cart is empty. No order created.").build();
            }
            int result = ordersService.createOrder(customerUid, data.getAddressUid(), data.getCouponUid(), data.getCardUid(), total);
            if (result == 0) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            else if (result == -5) {
                return Response.ok("The given coupon is not valid. No order created.").build();
            }
            
            uri = uriInfo.getBaseUriBuilder()
                    .path(OrdersResource.class)
                    .path(OrdersResource.class, "getOrder")
                    .build(result);
            
            return Response.created(uri).entity(uri.toString()).build();
        } catch (SQLException ex) {
            System.out.println(ex);
            return Response.serverError().build();
        }
    }
    
}
