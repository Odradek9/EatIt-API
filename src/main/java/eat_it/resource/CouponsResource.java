/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eat_it.resource;

import eat_it.model.Coupon;
import eat_it.security.Logged;
import eat_it.service.CouponsService;
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
@Path("/coupons")
public class CouponsResource {

    CouponsService couponsService = new CouponsService();
    CustomersService customersService = new CustomersService();
    
    @POST
    @Logged
    @RolesAllowed({"ADMIN"})
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postCoupon(Coupon coupon, @Context UriInfo uriInfo) {
        
        URI uri;
        
        try {
            String result = couponsService.createNewCoupon(coupon);
            if (result == null) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
            
            uri = uriInfo.getBaseUriBuilder()
                    .path(getClass())
                    .path(getClass(), "getCoupon")
                    .build(result);
            
            return Response.created(uri).entity(uri.toString()).build();
        } catch (SQLException ex) {
            System.out.println(ex);
            return Response.serverError().build();
        }
    }
    
    @GET
    @Logged
    @RolesAllowed({"ADMIN", "CUSTOMER"})
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCoupons(@Context ContainerRequestContext req, @Context UriInfo uriInfo) {
        
        try {
            
            if (customersService.isUserAdmin((String) req.getProperty("email"))) {
                ArrayList<URI> result = couponsService.getCoupons(uriInfo);
                if (result.isEmpty()) {
                    return Response.status(Response.Status.NOT_FOUND).build();
                }

                return Response.ok(result).build();
            }
            else {
                ArrayList<URI> result = couponsService.getCustomerCoupons((Integer) req.getProperty("id"), uriInfo);
                if (result.isEmpty()) {
                    return Response.status(Response.Status.NOT_FOUND).build();
                }

                return Response.ok(result).build();
            }
            
        } catch (SQLException ex) {
            System.out.println(ex);
            return Response.serverError().build();
        }
    }
    
    @Path("/{CoupUID}")
    @GET
    @Logged
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCoupon(@PathParam("CoupUID") String couponUid) {
        
        try {
            Coupon result = couponsService.getCoupon(couponUid);
            if (result.getUid() == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            
            return Response.ok(result).build();
        } catch (SQLException ex) {
            System.out.println(ex);
            return Response.serverError().build();
        }
    }
}
