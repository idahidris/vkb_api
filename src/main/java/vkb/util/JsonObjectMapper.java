package vkb.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonObjectMapper extends ObjectMapper {
    
    public JsonObjectMapper() {
        init(true);
    }
    
    public JsonObjectMapper(boolean nonNull) {
        init(nonNull);
    }
    
    private void init(boolean nonNull) {
        
        
        // Include only non-null properties
        if (nonNull) {
            setSerializationInclusion(JsonInclude.Include.NON_NULL);
        }
        
        // Normalization of line-endings
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter()
                .withObjectIndenter(new DefaultIndenter("  ", "\n"));
        setDefaultPrettyPrinter(prettyPrinter);
    }
}