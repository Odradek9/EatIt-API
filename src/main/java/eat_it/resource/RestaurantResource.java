/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eat_it.resource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import eat_it.jackson.ObjectMapperContextResolver;
import eat_it.model.Restaurant;
import eat_it.service.RestaurantService;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author anton
 */
@Path("contacts")
public class RestaurantResource {
    
    RestaurantService restaurantService = new RestaurantService();
    
    @GET
    public Response getContacts() {
        
        try {
            Restaurant result = restaurantService.getContacts();
            //System.out.println(restaurant.toString());
            if (result == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            ObjectMapperContextResolver o = new ObjectMapperContextResolver();
            ObjectMapper objectMapper = o.getContext(Restaurant.class);
            return Response.ok(objectMapper.writeValueAsString(result)).build();
        } catch (SQLException ex) {
            System.out.println(ex);
            return Response.serverError().build();
        } catch (JsonProcessingException ex) {
            return Response.serverError().build();
        }
        
    }
    
}
