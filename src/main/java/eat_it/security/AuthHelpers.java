package eat_it.security;

import eat_it.service.CustomersService;
import jakarta.ws.rs.core.UriInfo;
import java.sql.SQLException;

/**
 *
 * Una classe di utilità di supporto all'autenticazione 
 * qui usiamo JWT per tutte le operazioni
 *
 */
public class AuthHelpers {
    
    CustomersService customersService = new CustomersService();
    private static AuthHelpers instance = null;
    private final JWTHelpers jwt;
    
    public AuthHelpers() {
        jwt = JWTHelpers.getInstance();
    }
    
    public boolean authenticateUser(String email, String password) {
        try {
            return customersService.authUser(email, password);
        } catch (SQLException ex) {
            return false;
        }
    }
    
    public String issueToken(UriInfo context, String email) {
        return jwt.issueToken(context, email);
    }
    
    public void revokeToken(String token) {
        jwt.revokeToken(token);
    }
    
    public String validateToken(String token) {
        return jwt.validateToken(token);
    }
    
    public static AuthHelpers getInstance() {
        if (instance == null) {
            instance = new AuthHelpers();
        }
        return instance;
    }
    
}
