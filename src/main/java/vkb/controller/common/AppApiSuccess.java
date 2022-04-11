package vkb.controller.common;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * defines why an API call is successful
 */

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
final class AppApiSuccess extends Model implements Serializable {
    
    private static final long serialVersionUID = 3523937365043096440L;
    
    private String code = "";
    private String message = "";
    
    AppApiSuccess() {
    }
    
    AppApiSuccess(String code, String message) {
        if (code == null) code = "0";
        this.code = code;
        this.message = message;
    }
    
}
