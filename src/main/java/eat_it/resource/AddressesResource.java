/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eat_it.resource;

import eat_it.model.Address;
import eat_it.restApp.DatabaseException;
import eat_it.security.Logged;
import eat_it.service.AddressesService;
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
public class AddressesResource {
    
    AddressesService addressesService = new AddressesService();
    @Context ContainerRequestContext req;
    
    @POST
    @Logged
    @RolesAllowed({"CUSTOMER"})
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postAddress(@PathParam("CustomerUID") int customerUid, Address address, @Context UriInfo uriInfo, @Context ContainerRequestContext req) {
        
        if (!req.getProperty("id").equals(customerUid)) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        
        URI uri;
        
        try {
            int result = addressesService.createNewCustomerAddress(customerUid, address);
            
            uri = uriInfo.getBaseUriBuilder()
                    .path(CustomersResource.class)
                    .path(CustomersResource.class, "getAddressesResource")
                    .path(getClass(), "getAddress")
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
    
    @Path("/{AddUID}")
    @GET
    @Logged
    @RolesAllowed({"CUSTOMER"})
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAddress(@PathParam("AddUID") int addressUid, @PathParam("CustomerUID") int customerUid, @Context ContainerRequestContext req) {
        
        if (!req.getProperty("id").equals(customerUid)) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        
        try {
            Address result = addressesService.getCustomerAddress(customerUid, addressUid);
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
    public Response getAddresses(@PathParam("CustomerUID") int customerUid, @Context UriInfo uriInfo, @Context ContainerRequestContext req) {
        
        if (!req.getProperty("id").equals(customerUid)) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        
        try {
            ArrayList<URI> result = addressesService.getCustomerAddresses(customerUid, uriInfo);
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
