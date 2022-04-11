package vkb.controller.common;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * refines all the api error responses sent to the calling client
 * it is used in AppApiResponse
 */

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class AppApiError extends Model {
    
    
    private static final long serialVersionUID = 8594436616409629421L;
    
    /**
     * "userMessage": "Sorry, the requested resource does not exist",
     * 05
     * "developerMessage": "No car found in the database",
     * 06
     * "code": 34,
     * 07
     * "more info": "http://dev.mwaysolutions.com/blog/api/v1/errors/12345"
     * <p>
     * https://stormpath.com/blog/spring-mvc-rest-exception-handling-best-practices-part-1
     * https://stormpath.com/blog/spring-mvc-rest-exception-handling-best-practices-part-2
     * <p>
     * https://www.twilio.com/docs/api/errors/reference
     * <p>
     * The userMessage property is a nice human readable error message that can potentially be shown directly to an application end user (not a developer).
     * The errorCode property is an error code specific to your particular REST API. It is usually something that conveys information very specific to your problem domain.
     */
    
    private String message = ""; //
    private String errorCode = "";
    
    
    public AppApiError(String errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }
}
