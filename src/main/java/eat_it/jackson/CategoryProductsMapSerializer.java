package eat_it.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import eat_it.model.CategoryProductsMap;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Map;

public class CategoryProductsMapSerializer extends JsonSerializer<CategoryProductsMap> {

    @Override
    public void serialize(CategoryProductsMap map, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        
        Map<URI, ArrayList<URI>> fullMap = map.getMap();
        
        jgen.writeStartObject();
        
        for (URI uri : fullMap.keySet()) {
            jgen.writeObjectFieldStart(uri.toString());
            
            jgen.writeStringField("best", fullMap.get(uri).get(0).toString());
            jgen.writeStringField("worst", fullMap.get(uri).get(1).toString());
            
            jgen.writeEndObject();
        }
        
        jgen.writeEndObject();
        
        /**
        for (Category category : map.keySet()) {
            jgen.write
            jgen.writeNumberField("uid", category.getUid());
            jgen.writeStringField("name", category.getName());
            jgen.writeStartArray();
            
            for (Product product : map.get(category)) {
                
            }
            
        }
        jgen.writeEndObject();
        
        
        jgen.writeStringField("uid", );
        jgen.writeObjectField("start", item.getStart());
        jgen.writeObjectField("end", item.getEnd());
        jgen.writeObjectField("summary", item.getSummary());
        jgen.writeObjectField("categories", item.getCategories());
        jgen.writeObjectFieldStart("details");
        jgen.writeNumberField("duration", ChronoUnit.MINUTES.between(item.getStart(), item.getEnd()));
        jgen.writeEndObject();
        jgen.writeEndObject();
        */
    }
}
