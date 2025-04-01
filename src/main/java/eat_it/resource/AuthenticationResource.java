package eat_it.resource;

import eat_it.model.Customer;
import eat_it.security.AuthHelpers;
import eat_it.security.Logged;
import eat_it.service.CustomersService;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.NewCookie;
import jakarta.ws.rs.core.Response;
import static jakarta.ws.rs.core.Response.Status.UNAUTHORIZED;
import jakarta.ws.rs.core.UriInfo;
import java.net.URI;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Giuseppe Della Penna
 */
@Path("auth")
public class AuthenticationResource {
    
    @POST
    @Path("login")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response login(@Context UriInfo uriinfo, @FormParam("email") String email, @FormParam("password") String password) {
        
        try {
            
            if (AuthHelpers.getInstance().authenticateUser(email, password)) {
                
                String authToken = AuthHelpers.getInstance().issueToken(uriinfo, email);
                //Restituiamolo in tutte le modalità, giusto per fare un esempio...
                return Response.ok(authToken)
                        .cookie(new NewCookie.Builder("token").value(authToken).build())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + authToken).build();
            }
        } catch (Exception e) {
            System.out.println(e);
            Logger.getLogger(AuthenticationResource.class.getName()).log(Level.SEVERE, null, e);
        }
        return Response.status(UNAUTHORIZED).build();
    }
    
    @DELETE
    @Path("logout")
    @Logged
    public Response logout(@Context ContainerRequestContext req) {
        //proprietà estratta dall'authorization header 
        //e iniettata nella request dal filtro di autenticazione
        String token = (String) req.getProperty("token");
        AuthHelpers.getInstance().revokeToken(token);
        return Response.noContent()
                //eliminaimo anche il cookie con il token
                .cookie(new NewCookie.Builder("token").value("").maxAge(0).build())
                .build();
    }

    //Metodo per fare "refresh" del token senza ritrasmettere le credenziali
    @GET
    @Path("refresh")
    @Logged
    public Response refresh(@Context ContainerRequestContext req, @Context UriInfo uriinfo) {
        //proprietà iniettata nella request dal filtro di autenticazione
        String email = (String) req.getProperty("email");
        String newtoken = AuthHelpers.getInstance().issueToken(uriinfo, email);
         return Response.ok(newtoken)
                .cookie(new NewCookie.Builder("token").value(newtoken).build())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + newtoken).build();
    }
    
    @POST
    @Path("signup")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response signup(Customer customer, @Context UriInfo uriInfo) {
        
        try {
            CustomersService customersService = new CustomersService();
            
            URI uri = uriInfo.getBaseUriBuilder()
                    .path("/customers/{CustomerUID}")
                    .build(customersService.addCustomer(customer));
            
            return Response.created(uri).entity(uri.toString()).build();
        } catch (SQLException ex) {
            System.out.println(ex);
            return Response.serverError().build();
        }
        
    }
    
}
