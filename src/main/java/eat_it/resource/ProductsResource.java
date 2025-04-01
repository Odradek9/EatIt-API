/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eat_it.resource;

import eat_it.model.Product;
import eat_it.security.Logged;
import eat_it.service.ProductsService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
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
 * TO REDO, MIGHT NOT BE A GOOD IDEA TO HAVE THIS METHOD IN THE REST STRUCTURE
 */

/**
 * 
 * @author anton
 */
public class ProductsResource {
    
    ProductsService productsService = new ProductsService();
    
    /**************************************************************************/
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCategoryProducts(@PathParam("CatUID") int catUid){
        
        try {
            ArrayList<Product> result = productsService.getProductsByCat(catUid);
            if (result == null) {
                return Response.status(Status.NOT_FOUND).build();
            }
            else {
                return Response.ok(result).build();
            }
        } catch (SQLException ex) {
            System.out.println(ex);
            return Response.serverError().build();
        }
    }
    
    @POST
    @RolesAllowed({"ADMIN"})
    @Logged
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response addProduct(@PathParam("CatUID") int catUid, Product product, @Context UriInfo uriInfo){
        
        URI uri;
        try {
            int result = productsService.storeProduct(product, catUid);
            
            if (result == 0) {
                Response.status(Status.NOT_FOUND).build();
            }
            
            uri = uriInfo.getBaseUriBuilder()
                    .path(CategoriesResource.class)
                    .path(CategoriesResource.class, "getProductsResource")
                    .path(getClass(), "getCategoryProduct")
                    .build(catUid, result);
            
            return Response.created(uri).entity(uri.toString()).build();
        } catch (SQLException ex) {
            System.out.println(ex);
            return Response.serverError().build();
        }
    }
    
    @Path("{ProdUID}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCategoryProduct(@PathParam("CatUID") int catUid, @PathParam("ProdUID") int prodUid){
        
        try {
            Product result = productsService.getProduct(prodUid, catUid);
            if (result.getUid() == null) {
                return Response.status(Status.NOT_FOUND).build();
            }
            
            return Response.ok(result).build();
        } catch (SQLException ex) {
            System.out.println(ex);
            return Response.serverError().build();
        }
    }
    
    @PUT
    @RolesAllowed({"ADMIN"})
    @Logged
    public Response putCategoryProduct(@PathParam("CatUID") int catUid, Product product){
        
        try {
            boolean check = productsService.updateProduct(product, catUid, product.getUid());
            if (!check) {
                return Response.status(Status.NOT_FOUND).build();
            }
            return Response.noContent().build();
        } catch (SQLException ex) {
            System.out.println(ex);
            return Response.serverError().build();
        }
    }
    
    @Path("{ProdUID}")
    @DELETE
    @RolesAllowed({"ADMIN"})
    @Logged
    public Response deleteCategoryProduct(@PathParam("CatUID") int catUid, @PathParam("ProdUID") int prodUid){
        
        try {
            boolean check = productsService.deleteProduct(catUid, prodUid);
            if (!check) {
                return Response.status(Status.NOT_FOUND).build();
            }           
            return Response.noContent().build();
        } catch (SQLException ex) {
            System.out.println(ex);
            return Response.serverError().build();
        }
    }
    
    /**************************************************************************/
    
    @Path("{ProdUID}/reviews")
    public ReviewsResource getReviewsResource(){
        
        return new ReviewsResource();
    }
    
}
