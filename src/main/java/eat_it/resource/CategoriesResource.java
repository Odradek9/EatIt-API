/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eat_it.resource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import eat_it.jackson.ObjectMapperContextResolver;
import eat_it.service.CategoriesService;
import eat_it.model.Category;
import eat_it.model.CategoryProductsMap;
import eat_it.security.Logged;
import eat_it.service.ProductsService;
import eat_it.service.ReviewsService;
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
 * @author anton
 */
@Path("categories")
public class CategoriesResource {
    
    CategoriesService categoriesService = new CategoriesService();
    ProductsService productService = new ProductsService();
    ReviewsService reviewsService = new ReviewsService();
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCategories(@Context UriInfo uriInfo) {
        
        try {
            ArrayList<URI> result = categoriesService.getAllCategories(uriInfo);
            if (result == null) {
                return Response.serverError().build();
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
    @Logged
    @RolesAllowed({"ADMIN"})
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postCategory(Category category, @Context UriInfo uriInfo) {
        
        try {
            int result = categoriesService.addCategory(category);
            if (result == 0) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
            else {
                
                URI uri = uriInfo.getBaseUriBuilder()
                    .path(CategoriesResource.class)
                    .path(CategoriesResource.class, "getCategoryInfo")
                    .build(result);
                return Response.created(uri).entity(uri.toString()).build();
            }
        } catch (SQLException ex) {
            System.out.println(ex);
            return Response.serverError().build();
        }
    }
    
    @PUT
    @Logged
    @RolesAllowed({"ADMIN"})
    @Consumes(MediaType.APPLICATION_JSON)
    public Response putCategory(Category category) {
        
        try {
            boolean check = categoriesService.modifyCategory(category);
            if (check == false) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
            else {
                
                return Response.noContent().build();
            }
        } catch (SQLException ex) {
            System.out.println(ex);
            return Response.serverError().build();
        }
    }
    
    @Path("/BestWorstSellers")
    @GET
    @Logged
    @RolesAllowed({"ADMIN"})
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBesWorsttSellers(@Context UriInfo uriInfo) {
        
        try {
            CategoryProductsMap result = categoriesService.getBesWorsttSellers(uriInfo);
            if (result == null) {
                return Response.serverError().build();
            }
            else {
                ObjectMapperContextResolver o = new ObjectMapperContextResolver();
                ObjectMapper objectMapper = o.getContext(CategoryProductsMap.class);
                return Response.ok(objectMapper.writeValueAsString(result)).build();
            }
        } catch (SQLException ex) {
            System.out.println(ex);
            return Response.serverError().build();
        } catch (JsonProcessingException ex) {
            System.out.println(ex);
            return Response.serverError().build();
        }
    }
    
    /**************************************************************************/
    
    @Path("/{CatUID}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCategoryInfo(@PathParam("CatUID") int catUid) {
        
        try {
            Category result = categoriesService.getCategory(catUid);
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
    
    @Path("/{CatUID}")
    @DELETE
    @Logged
    @RolesAllowed({"ADMIN"})
    public Response deleteCategory(@PathParam("CatUID") int catUid) {
        
        try {
            boolean result = categoriesService.deleteCategory(catUid);
            if (result == false) {
                return Response.status(Status.NOT_FOUND).build();
            }
            else {
                return Response.noContent().build();
            }
        } catch (SQLException ex) {
            System.out.println(ex);
            return Response.serverError().build();
        }
    }
    
    @Path("/{CatUID}/products")
    public ProductsResource getProductsResource(){
        
        return new ProductsResource();
    }
    
    /**************************************************************************/
}
