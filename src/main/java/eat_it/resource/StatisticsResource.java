/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eat_it.resource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import eat_it.jackson.ObjectMapperContextResolver;
import eat_it.model.CategoryProductsMap;
import eat_it.model.Statistics;
import eat_it.security.Logged;
import eat_it.service.StatisticsService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author anton
 */
@Path("statistics")
public class StatisticsResource {
    
    StatisticsService statisticsService = new StatisticsService();
    
    @GET
    @Logged
    @RolesAllowed({"ADMIN"})
    public Response getStatistics(@Context UriInfo uriInfo) {
        
        try {
            Statistics result = statisticsService.getStatistics(uriInfo);
            if(result.getMonthlyOrders() == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            ObjectMapperContextResolver o = new ObjectMapperContextResolver();
            ObjectMapper objectMapper = o.getContext(Statistics.class);
            return Response.ok(objectMapper.writeValueAsString(result)).build();
        } catch (SQLException ex) {
            System.out.println(ex);
            return Response.serverError().build();
        } catch (JsonProcessingException ex) {
            return Response.serverError().build();
        }
        
    }
    
}
