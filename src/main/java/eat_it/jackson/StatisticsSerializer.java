/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eat_it.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import eat_it.model.Statistics;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Map;

/**
 *
 * @author anton
 */
public class StatisticsSerializer extends JsonSerializer<Statistics> {

    @Override
    public void serialize(Statistics t, JsonGenerator jgen, SerializerProvider sp) throws IOException {
        
        
        Map<String, Integer> map = t.getMonthlyOrders();
        
        jgen.writeStartObject();
        
        jgen.writeNumberField("monthlyRevenues", t.getMontlyRevenues());
        jgen.writeStringField("bestSeller", t.getBestSeller().toString());
        jgen.writeStringField("bestSeller", t.getWorstSeller().toString());
        
        jgen.writeObjectFieldStart("ordersPerMonth");
        for (String month : map.keySet()) {
            
            jgen.writeNumberField(month, map.get(month));
            
        }
        
        jgen.writeEndObject();
    }
    
}
