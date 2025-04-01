/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eat_it.resource;

import eat_it.model.Review;
import eat_it.security.Logged;
import eat_it.service.ReviewsService;
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
import jakarta.ws.rs.core.UriInfo;
import java.net.URI;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author anton
 */
public class ReviewsResource {

    private ReviewsService reviewsService = new ReviewsService();
    
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProductReviews(@PathParam("CatUID") int catUid, @PathParam("ProdUID") int prodUid, @Context UriInfo uriInfo){
        
        try {
            ArrayList<URI> result = reviewsService.getReviews(prodUid, catUid, uriInfo);
            if(result.isEmpty()) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            return Response.ok(result).build();
        } catch (SQLException ex) {
            System.out.println(ex);
            return Response.serverError().build();
        }
    }
    
    @POST
    @Logged
    @RolesAllowed({"CUSTOMER"})
    @Consumes(MediaType.APPLICATION_JSON)
    public Response PostProductReview(@PathParam("CatUID") int catUid, @PathParam("ProdUID") int prodUid, Review review, @Context UriInfo uriInfo, @Context ContainerRequestContext req){
        
        review.setCustomerUid((Integer) req.getProperty("id"));
        
        System.out.println(review.getCustomerUid());
        
        try {
            int resultUid = reviewsService.postReview(prodUid, catUid, review);
            if (resultUid == 0) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            URI uri = uriInfo.getBaseUriBuilder()
                    .path(CategoriesResource.class)
                    .path(CategoriesResource.class, "getProductsResource")
                    .path(ProductsResource.class, "getReviewsResource")
                    .path(getClass(), "getProductReview")
                    .build(catUid, prodUid, resultUid);
            
            return Response.created(uri).entity(uri.toString()).build();
        } catch (SQLException ex) {
            System.out.println(ex);
            return Response.serverError().build();
        }
    }
    
    @Path("{RevUID}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProductReview(@PathParam("CatUID") int catUid, @PathParam("ProdUID") int prodUid, @PathParam("RevUID") int revUid){
        
        try {
            Review result = reviewsService.getReview(prodUid, catUid, revUid);
            if (result.getUid() == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            return Response.ok(result).build();
        } catch (SQLException ex) {
            System.out.println(ex);
            return Response.serverError().build();
        }
    }
    
    @Path("{RevUID}")
    @DELETE
    @RolesAllowed({"CUSTOMER"})
    @Logged
    public Response deleteProductReview(@PathParam("CatUID") int catUid, @PathParam("ProdUID") int prodUid, @PathParam("RevUID") int revUid, @Context ContainerRequestContext req){
        
        try {
            int customerUid = reviewsService.getCustomerUidOfReview(revUid);
            if (customerUid == 0) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            else {
                if (!req.getProperty("id").equals(customerUid)) {
                    return Response.status(Response.Status.UNAUTHORIZED).build();
                }
            }
            
            boolean check = reviewsService.deleteProductReview(catUid, prodUid, revUid);
            if (!check) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            return Response.noContent().build();
        } catch (SQLException ex) {
            System.out.println(ex);
            return Response.serverError().build();
        }
    }
}
