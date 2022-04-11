package vkb.controller.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;


@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)

public class AppApiWarning extends Model implements Serializable {
    
    
    private static final long serialVersionUID = 3917571513829109466L;
    /**
     * "userMessage": "Sorry, the requested resource does not exist",
     * 05
     * "developerMessage": "No car found in the database",
     * 06
     * "code": 34,
     * 07
     * "more info": "http://dev.mwaysolutions.com/blog/api/v1/errors/12345"
     */
    private String userMessage = "";
    private String internalMessage = "";
    private String code = "";
    private String moreInfo = "";
    
    public AppApiWarning() {
    }
    
    public AppApiWarning(String code, String internalMessage) {
        this.code = code;
        this.internalMessage = internalMessage;
    }
    
}
