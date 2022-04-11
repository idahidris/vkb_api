package vkb.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;



@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {
    
    private final String resourceName;
    
    
    public ResourceNotFoundException(String resourceName) {
        this.resourceName = resourceName;
    }
    
    public String getResourceName() {
        return resourceName;
    }
    
    
}
