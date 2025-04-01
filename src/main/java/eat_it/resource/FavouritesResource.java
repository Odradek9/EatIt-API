/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eat_it.resource;

import eat_it.model.Product;
import eat_it.security.Logged;
import eat_it.service.FavouritesService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author anton
 */
public class FavouritesResource {
    
    FavouritesService favouritesService = new FavouritesService();
    
    @GET
    @Logged
    @RolesAllowed({"CUSTOMER"})
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFavourites(@PathParam("CustomerUID") int customerUid, @Context ContainerRequestContext req){
        
        if (!req.getProperty("id").equals(customerUid)) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        
        try {
            ArrayList<Product> result = favouritesService.getCustomerFavourites(customerUid);
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
    
    @POST
    @Logged
    @RolesAllowed({"CUSTOMER"})
    @Consumes(MediaType.TEXT_PLAIN)
    public Response addProductToCustomerFavourites(@PathParam("CustomerUID") int customerUid, int prodUid, @Context ContainerRequestContext req) {
        
        if (!req.getProperty("id").equals(customerUid)) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        
        try {
            boolean check = favouritesService.addProductToFavourites(customerUid, prodUid);
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
    
    @Path("/{ProdUID}")
    @DELETE
    @RolesAllowed({"CUSTOMER"})
    @Logged
    public Response deleteProductFromCustomerFavourites(@PathParam("ProdUID") int prodUid, @PathParam("CustomerUID") int customerUid, @Context ContainerRequestContext req) {
        
        if (!req.getProperty("id").equals(customerUid)) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        
        try {
            boolean check = favouritesService.deleteProductFromFavourites(customerUid, prodUid);
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
    
}
