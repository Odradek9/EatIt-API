/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eat_it.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import eat_it.model.Address;
import eat_it.model.Restaurant;
import java.io.IOException;

/**
 *
 * @author anton
 */
public class RestaurantSerializer extends JsonSerializer<Restaurant> {

    @Override
    public void serialize(Restaurant r, JsonGenerator jg, SerializerProvider sp) throws IOException {
        
        jg.writeStartObject();
        
        jg.writeStringField("name", r.getName());
        jg.writeStringField("email", r.getEmail());
        
        Address address = r.getAddresses().get(0);
        jg.writeObjectField("address", address);
        
        jg.writeStringField("email", r.getPhone().toString());
        
        jg.writeEndObject();
        
    }
    
}
