package vkb.controller.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * in the case of more than one api error,
 * this class gathers the errors in a list and also provides a count feature
 */


@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)

public class AppApiErrors extends Model {
    
    
    private static final long serialVersionUID = -8274298103033996950L;
    
    private int errorCount;
    private List<AppApiError> apiErrorList = new ArrayList<>();
    
    public AppApiErrors() {
    }
    

    public AppApiErrors(int errorCount, List<AppApiError> apiErrorList) {
        this.errorCount = errorCount;
        this.apiErrorList = apiErrorList;
    }
}
