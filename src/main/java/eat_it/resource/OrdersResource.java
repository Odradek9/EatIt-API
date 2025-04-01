/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eat_it.resource;

import eat_it.model.JsonData;
import eat_it.model.Order;
import eat_it.security.Logged;
import eat_it.service.CartsService;
import eat_it.service.CustomersService;
import eat_it.service.OrdersService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.core.UriInfo;
import java.net.URI;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author anton
 */
@Path("orders")
public class OrdersResource {
    
    OrdersService ordersService = new OrdersService();
    CartsService cartsService = new CartsService();
    CustomersService customersService = new CustomersService();
    
    
    @GET
    @Logged
    @RolesAllowed({"ADMIN"})
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOrders(@Context UriInfo uriInfo) {
        
        try {
            ArrayList<URI> result = ordersService.getOrders(uriInfo);
            if (result.isEmpty()) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            
            return Response.ok(result).build();
        } catch (SQLException ex) {
            System.out.println(ex);
            return Response.serverError().build();
        }
    }
    
    @Path("/{OrderUID}")
    @GET
    @Logged
    @RolesAllowed({"ADMIN", "CUSTOMER"})
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOrder(@PathParam("OrderUID") int orderUid) {
        
        try {
            Order result = ordersService.getOrder(orderUid);
            if (result.getUid() == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            
            return Response.ok(result).build();
        } catch (SQLException ex) {
            System.out.println(ex);
            return Response.serverError().build();
        }
    }
    
    @Path("/{OrderUID}/orderToCart")
    @POST
    @Logged
    @RolesAllowed({"CUSTOMER"})
    public Response createCartFromOrder(@PathParam("OrderUID") int orderUid, @Context ContainerRequestContext req) {
        
        try {
            
            if (!customersService.isOrderOfCustomer((Integer) req.getProperty("id"), orderUid)) {
                return Response.status(Status.UNAUTHORIZED).build();
            }
            
            boolean check = cartsService.createCartFromOrder(orderUid);
            if (!check) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            
            return Response.noContent().build();
        } catch (SQLException ex) {
            System.out.println(ex);
            return Response.serverError().build();
        }
    }
    
    @Path("/{OrderUID}/accept")
    @POST
    @Logged
    @RolesAllowed({"ADMIN"})
    public Response acceptOrder(@PathParam("OrderUID") int orderUid){
        
        try {
            
            boolean check = ordersService.acceptOrder(orderUid);
            if (!check) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            
            return Response.noContent().build();
        } catch (SQLException ex) {
            System.out.println(ex);
            return Response.serverError().build();
        }
    }
    
    @Path("/{OrderUID}/refuse")
    @POST
    @Logged
    @RolesAllowed({"ADMIN"})
    public Response refuseOrder(@PathParam("OrderUID") int orderUid){
        
        try {
            
            boolean check = ordersService.refuseOrder(orderUid);
            if (!check) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            
            return Response.noContent().build();
        } catch (SQLException ex) {
            System.out.println(ex);
            return Response.serverError().build();
        }
    }
    
    @Path("/{OrderUID}/confirm")
    @POST
    @Logged
    @RolesAllowed({"CUSTOMER"})
    public Response confirmOrder(@PathParam("OrderUID") int orderUid, @Context ContainerRequestContext req){
        
        try {
            
            if (!customersService.isOrderOfCustomer((Integer) req.getProperty("id"), orderUid)) {
                return Response.status(Response.Status.UNAUTHORIZED).build();
            }
            
            boolean check = ordersService.confirmOrder(orderUid);
            if (!check) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            
            return Response.noContent().build();
        } catch (SQLException ex) {
            System.out.println(ex);
            return Response.serverError().build();
        }
    }
    
    @Path("/{OrderUID}/products")
    @GET
    @Logged
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOrderProducts(@PathParam("OrderUID") int orderUid) {
        
        try {
            //Get custom serializer to turn the productUID into URLs
            ArrayList<JsonData> result = ordersService.getOrderProducts(orderUid);
            if (result.isEmpty()) {
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
}
