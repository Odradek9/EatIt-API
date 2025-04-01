/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eat_it.resource;

import eat_it.model.Product;
import eat_it.service.ProductsService;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
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
@Path("/products")
public class ProductsCollectiveResource {
    
    ProductsService productService = new ProductsService();
    
    /**
     *
     * @return an ArrayList of Product class objects
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProducts(@Context UriInfo uriInfo){
        
        try {
            ArrayList<URI> result = productService.getAllProducts(uriInfo);
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
