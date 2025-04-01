/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eat_it.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import eat_it.model.JsonData;
import eat_it.model.JsonList;
import java.io.IOException;

/**
 *
 * @author anton
 */
public class ProdQuantityListSerializer extends JsonSerializer<JsonList> {

    @Override
    public void serialize(JsonList t, JsonGenerator jg, SerializerProvider sp) throws IOException {
        
        jg.writeStartObject();
        
        for (JsonData jsonData : t.getList()) {
            
            if(jsonData.getProductURI() != null) {
                jg.writeNumberField(jsonData.getProductURI().toString(), jsonData.getQuantity());
            }
            
            
        }
        
        jg.writeEndObject();
        
    }
    
}
