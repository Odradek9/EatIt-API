/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package eat_it.restApp;

import com.fasterxml.jackson.jakarta.rs.json.JacksonJsonProvider;
import eat_it.jackson.ObjectMapperContextResolver;
import eat_it.resource.AddressesResource;
import eat_it.resource.AuthenticationResource;
import eat_it.resource.CardsResource;
import eat_it.resource.CartsResource;
import eat_it.resource.CategoriesResource;
import eat_it.resource.CouponsResource;
import eat_it.resource.CustomersResource;
import eat_it.resource.FavouritesResource;
import eat_it.resource.OrdersResource;
import eat_it.resource.ProductsCollectiveResource;
import eat_it.resource.ProductsResource;
import eat_it.resource.RestaurantResource;
import eat_it.resource.ReviewsResource;
import eat_it.resource.StatisticsResource;
import eat_it.security.AuthLoggedFilter;
import eat_it.security.CORSFilter;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


/**
 *
 * @author anton
 *
 */
@ApplicationPath("/rest/")
public class RESTapplication extends Application{
    
    private final Set<Class<?>> classes;

    public RESTapplication() {
        HashSet<Class<?>> c = new HashSet<>();
        //aggiungiamo tutte le *root resurces* (cioè quelle
        //con l'annotazione Path) che vogliamo pubblicare
        c.add(CategoriesResource.class);
        c.add(AuthenticationResource.class);
        c.add(CustomersResource.class);
        c.add(ProductsResource.class);
        c.add(AddressesResource.class);
        c.add(CardsResource.class);
        c.add(CartsResource.class);
        c.add(CouponsResource.class);
        c.add(FavouritesResource.class);
        c.add(OrdersResource.class);
        c.add(ProductsCollectiveResource.class);
        c.add(ReviewsResource.class);
        c.add(RestaurantResource.class);
        c.add(StatisticsResource.class);
        
        c.add(JacksonJsonProvider.class);
        
        //necessario se vogliamo una (de)serializzazione custom di qualche classe    
        c.add(ObjectMapperContextResolver.class);

        //esempio di autenticazione
        c.add(AuthLoggedFilter.class);

        //aggiungiamo il filtro che gestisce gli header CORS
        c.add(CORSFilter.class);

        classes = Collections.unmodifiableSet(c);
    }

    //l'override di questo metodo deve restituire il set
    //di classi che Jersey utilizzerà per pubblicare il
    //servizio. Tutte le altre, anche se annotate, verranno
    //IGNORATE
    @Override
    public Set<Class<?>> getClasses() {
        return classes;
    }
}
