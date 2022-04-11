package vkb.util;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class JsonUtil {
    
    private static final JsonObjectMapper jsonObjectMapper = new JsonObjectMapper(true);
    
    private JsonUtil() {
    }
    
    public static String toJson(Object object) {
        return toJson(object, false);
    }
    
    public static String toJson(Object object, boolean prettyPrint) {
        try {
            if (!prettyPrint) {
                return jsonObjectMapper.writeValueAsString(object);
            }
            return jsonObjectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize object to JSON " + object.getClass().getCanonicalName(), e);
        }
    }
    
    public  static <T> T toObject(String json, Class<T> clazz) {
        try {
            return jsonObjectMapper.readValue(json, clazz);
        } catch (IOException e) {
            throw new RuntimeException("Failed to deserialize JSON to object " + clazz.getCanonicalName(), e);
        }
    }
    
    public  static <T> Map<String, T> toObjectMap(String json, Class<T> valueClass) {
        try {
            TypeReference<HashMap<String, T>> typeRef = new TypeReference<HashMap<String, T>>() {
            };
            return jsonObjectMapper.readValue(json, typeRef);
        } catch (IOException e) {
            throw new RuntimeException("Failed to deserialize JSON to object map " + valueClass.getCanonicalName(), e);
        }
    }
    
    public static  <T> List<T> toObjectList(String json, Class<T> elementClass) {
        try {
            JavaType javaType = jsonObjectMapper.getTypeFactory().constructCollectionType(List.class, elementClass);
            return jsonObjectMapper.readValue(json, javaType);
        } catch (IOException e) {
            throw new RuntimeException("Failed to deserialize JSON to object list " + elementClass.getCanonicalName(), e);
        }
    }
}



