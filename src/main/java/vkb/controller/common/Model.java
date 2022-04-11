package vkb.controller.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import vkb.util.JsonUtil;
import lombok.EqualsAndHashCode;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
//used @JsonIgnoreProperties so that if there are more attributes in the response, they can be safely ignored by Spring
public abstract class Model implements Serializable {
    
    private static final long serialVersionUID = -4688878025545867889L;
    
    @Override
    public String toString() {
        return JsonUtil.toJson(this);
    }
    
    private void writeObject(ObjectOutputStream stream)
            throws IOException {
        stream.defaultWriteObject();
    }
    
    private void readObject(ObjectInputStream stream)
            throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
    }
}
