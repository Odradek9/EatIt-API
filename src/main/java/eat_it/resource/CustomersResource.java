/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eat_it.resource;

import eat_it.model.Customer;
import eat_it.security.Logged;
import eat_it.service.CartsService;
import eat_it.service.CustomersService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Context;
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
@Path("customers")
public class CustomersResource {
    
    CustomersService customersService = new CustomersService();
    CartsService cartsService = new CartsService();
    
    @Path("{CustomerUID}")
    @GET
    @Logged
    @RolesAllowed({"ADMIN", "CUSTOMER"})
    public Response getCustomer(@PathParam("CustomerUID") int customerUid) {
        
        try {
            Customer result = customersService.getCustomer(customerUid);
            if (result == null){
                return Response.status(Status.NOT_FOUND).build();
            }
            else {
                return Response.ok(result).build();
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return Response.serverError().build();
        }
    }
    
    @GET
    @Logged
    @RolesAllowed({"ADMIN"})
    public Response getCustomers(@PathParam("CustomerUID") int customerUid, @Context UriInfo uriInfo) {
        
        try {
            ArrayList<URI> result = customersService.getCustomers(uriInfo);
            if (result == null){
                return Response.status(Status.NOT_FOUND).build();
            }
            else {
                return Response.ok(result).build();
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return Response.serverError().build();
        }
    }
    
    @Path("best")
    @GET
    @Logged
    @RolesAllowed({"ADMIN"})
    public Response getBestCustomers(@PathParam("CustomerUID") int customerUid) {
        
        try {
            ArrayList<Customer> result = customersService.getBestCustomersOfMonth();
            if (result == null){
                return Response.status(Status.NOT_FOUND).build();
            }
            else {
                return Response.ok(result).build();
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return Response.serverError().build();
        }
    }
    
    @Path("{CustomerUID}/favourites")
    public FavouritesResource getFavouritesResource() {
        
        return new FavouritesResource();
    }
    
    @Path("{CustomerUID}/cart")
    public CartsResource getCartsResource() {
        
        return new CartsResource();
    }
    
    @Path("{CustomerUID}/favouritesToCart")
    @POST
    @Logged
    @RolesAllowed({"CUSTOMER"})
    public Response addFavouritesToCart(@PathParam("CustomerUID") int customerUid) {
        
        // doesnt work for whatever reason
        
        try {
            boolean check = cartsService.addFavouritesToCart(customerUid);
            if (check == false){
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            else{
                return Response.noContent().build();
            }
        } catch (SQLException ex) {
            System.out.println(ex);
            return Response.serverError().build();
        }
        
    }
    
    @Path("{CustomerUID}/cards")
    public CardsResource getCardsResource() {
        
        return new CardsResource();
    }
    
    @Path("{CustomerUID}/addresses")
    public AddressesResource getAddressesResource() {
        
        return new AddressesResource();
    }
    
}
