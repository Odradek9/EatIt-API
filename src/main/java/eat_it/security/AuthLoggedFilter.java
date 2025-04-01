package eat_it.security;

import eat_it.service.CustomersService;
import jakarta.annotation.Priority;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ResourceInfo;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ws.rs.ext.Provider;
import java.io.IOException;
import java.lang.reflect.Method;
import java.security.Principal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author didattica
 */
@Provider
@Logged
@Priority(Priorities.AUTHENTICATION)
public class AuthLoggedFilter implements ContainerRequestFilter {
    
    private CustomersService customersService = new CustomersService();
    
    @Context
    private ResourceInfo resourceInfo;
    
    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        
        String token = null;
        //final String path = requestContext.getUriInfo().getAbsolutePath().toString();
        
        //come esempio, proviamo a cercare il token in vari punti, in ordine di priorità
        //in un'applicazione reale, potremmo scegliere una sola modalità
        String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring("Bearer".length()).trim();
            //System.out.println("1: " + token);
        }/** else if (requestContext.getCookies().containsKey("token")) {
            token = requestContext.getCookies().get("token").getValue();
            //System.out.println("2: " + token);
        } else if (requestContext.getUriInfo().getQueryParameters().containsKey("token")) {
            token = requestContext.getUriInfo().getQueryParameters().getFirst("token");
            //System.out.println("3: " + token);
        }
        */
        if (token != null && !token.isEmpty()) {
            
            try {
                //validiamo il token
                final String email = AuthHelpers.getInstance().validateToken(token);
                //System.out.println(email);
                if (email != null) {
                    
                    Method method = resourceInfo.getResourceMethod();

                    if (method.isAnnotationPresent(javax.annotation.security.RolesAllowed.class)) {
                        javax.annotation.security.RolesAllowed rolesAnnotation = method.getAnnotation(javax.annotation.security.RolesAllowed.class);
                        Set<String> rolesSet = new HashSet<String>(Arrays.asList(rolesAnnotation.value()));

                        // Is user valid?
                        if(rolesSet.contains("ADMIN") && !customersService.isUserAdmin(email)) {
                            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
                            return;
                        }
                        else if (rolesSet.contains("CUSTOMER") && !customersService.isUserCustomer(email)) {
                            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
                            return;
                        }
                    }
                    
                    //inseriamo nel contesto i risultati dell'autenticazione
                    //per farli usare dai nostri metodi restful
                    //iniettando @Context ContainerRequestContext
                    requestContext.setProperty("token", token);
                    requestContext.setProperty("id", customersService.getCustomerUid(email));
                    requestContext.setProperty("email", email);
                    //OPPURE
                    //mettiamo i dati anche nel securitycontext standard di JAXRS...
                    //che può essere iniettato con @Context SecurityContext nei metodi
                    //final SecurityContext originalSecurityContext = requestContext.getSecurityContext();
                    /**
                    requestContext.setSecurityContext(new SecurityContext() {
                        @Override
                        public Principal getUserPrincipal() {
                            return new Principal() {
                                @Override
                                public String getName() {
                                    return email;
                                }
                            };
                        }

                        @Override
                        public boolean isUserInRole(String role) {
                            //qui andrebbe verificato se l'utente ha il ruolo richiesto
                            return true;
                        }

                        @Override
                        public boolean isSecure() {
                            return path.startsWith("https");
                        }

                        @Override
                        public String getAuthenticationScheme() {
                            return "Token-Based-Auth-Scheme";
                        }
                    });
                    */

                } else {
                    //se non va bene... 
                    requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
                requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
            }
        } else {
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
        }
    }
}
