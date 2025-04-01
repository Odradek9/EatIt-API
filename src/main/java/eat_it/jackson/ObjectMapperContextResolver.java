package eat_it.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import eat_it.model.CategoryProductsMap;
import eat_it.model.JsonList;
import eat_it.model.Restaurant;
import eat_it.model.Statistics;
import jakarta.ws.rs.ext.ContextResolver;
import jakarta.ws.rs.ext.Provider;

/**
 *
 * @author didattica
 */
@Provider
public class ObjectMapperContextResolver implements ContextResolver<ObjectMapper> {

    private final ObjectMapper mapper;

    public ObjectMapperContextResolver() {
        this.mapper = createObjectMapper();
    }

    @Override
    public ObjectMapper getContext(Class<?> type) {
        return mapper;
    }

    private ObjectMapper createObjectMapper() {
        ObjectMapper m = new ObjectMapper();
        //abilitiamo una feature nuova...
        m.enable(SerializationFeature.INDENT_OUTPUT);
        SimpleModule customSerializer = new SimpleModule("CustomSerializersModule");
        customSerializer.addSerializer(JsonList.class, new ProdQuantityListSerializer());
        customSerializer.addSerializer(CategoryProductsMap.class, new CategoryProductsMapSerializer());
        customSerializer.addSerializer(Restaurant.class, new RestaurantSerializer());
        customSerializer.addSerializer(Statistics.class, new StatisticsSerializer());

        //
        m.registerModule(customSerializer);

        //per il supporto alla serializzazione automatica dei tipi Date/Time di Java 8 (ZonedDateTime, LocalTime, ecc.)
        //è necessario aggiungere alle dipendenze la libreria com.fasterxml.jackson.datatype:jackson-datatype-jsr310
        //mapper.registerModule(new JavaTimeModule());
        //questa feature fa cercare a Jackson tutti i moduli compatibili inseriti nel contesto...
        m.findAndRegisterModules();
        //perchè le date appaiano "human readable"
        m.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        return m;
    }
}
