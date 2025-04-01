/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eat_it.resource;

import eat_it.model.CreditCard;
import eat_it.restApp.DatabaseException;
import eat_it.security.Logged;
import eat_it.service.CardsService;
import eat_it.service.CustomersService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
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
import java.util.ArrayList;

/**
 *
 * @author anton
 */
public class CardsResource {
    
    CardsService cardsService = new CardsService();
    CustomersService customersService = new CustomersService();
    
    @POST
    @Logged
    @RolesAllowed({"CUSTOMER"})
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postCreditCard(@PathParam("CustomerUID") int customerUid, CreditCard card, @Context UriInfo uriInfo, @Context ContainerRequestContext req) {
        
        if (!req.getProperty("id").equals(customerUid)) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        
        URI uri;
        
        try {
            int result = cardsService.createNewCustomerCard(customerUid, card);
            if (result == 0) {
                Response.status(Response.Status.BAD_REQUEST).build();
            }
            
            uri = uriInfo.getBaseUriBuilder()
                    .path(CustomersResource.class)
                    .path(CustomersResource.class, "getCardsResource")
                    .path(getClass(), "getCustomerCard")
                    .build(customerUid, result);
            
            return Response.created(uri).entity(uri.toString()).build();
        } catch (SQLException ex) {
            System.out.println(ex);
            return Response.serverError().build();
        } catch (DatabaseException ex) {
            System.out.println(ex);
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }
    
    @Path("/{CardUID}")
    @GET
    @Logged
    @RolesAllowed({"CUSTOMER"})
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCustomerCard(@PathParam("CustomerUID") int customerUid, @PathParam("CardUID") int cardUid, @Context ContainerRequestContext req) {
        
        try {
            if (!req.getProperty("id").equals(customerUid)) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
            }
            
            CreditCard result = cardsService.getCustomerCard(customerUid, cardUid);
            if (result.getUid() == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            
            return Response.ok(result).build();
        } catch (SQLException ex) {
            System.out.println(ex);
            return Response.serverError().build();
        }
    }
    
    @GET
    @Logged
    @RolesAllowed({"CUSTOMER"})
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCustomerCards(@PathParam("CustomerUID") int customerUid, @Context ContainerRequestContext req, @Context UriInfo uriInfo) {
        
        try {
            if (!req.getProperty("id").equals(customerUid)) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
            }
            
            ArrayList<URI> result = cardsService.getCustomerCards(customerUid, uriInfo);
            if (result.isEmpty()) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            
            return Response.ok(result).build();
        } catch (SQLException ex) {
            System.out.println(ex);
            return Response.serverError().build();
        }
    }
}
