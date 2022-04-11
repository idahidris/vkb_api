package vkb.controller.common;


import vkb.dto.PageDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StopWatch;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

import static lombok.AccessLevel.PACKAGE;
import static org.springframework.http.HttpStatus.OK;

/**
 * refines all the api responses sent to the calling client
 */

@Slf4j
@Data
@Component
@AllArgsConstructor(access = PACKAGE)
public class ApiResponseUtil {
    
    private final HttpServletRequest request;
    private final HttpServletResponse response;
    private final LocaleResolver localeResolver;
    
    private StopWatch watch;
    private long startTime = 0;
    
    @Autowired
    public ApiResponseUtil(HttpServletRequest request, HttpServletResponse response, LocaleResolver localeResolver) {
        this.request = request;
        this.response = response;
        this.localeResolver = localeResolver;
    }
    
    public AppApiResponse arrangeInitialResponse() {
        
        AppApiResponse apiResponseFromServletRequest = (AppApiResponse) request.getAttribute(AppApiResponse.class.getSimpleName());
        if (apiResponseFromServletRequest != null) {
            return apiResponseFromServletRequest;
        }
        
        logRouteStarted();
        
        watch = new StopWatch();
        watch.start();
        
        startTime = System.nanoTime();
        
        AppApiResponse apiResponse = AppApiResponse.builder().build();
        
        apiResponse.setApiErrors(new AppApiErrors());
        return apiResponse;
    }
    
    private void logRouteStarted() {
        log.info("*********************** " + getRouteID() + " - STARTED ******************************");
    }
    
    private void logRouteEnded() {
        log.info("\n\n******************************************************************************************" +
                "************************************************************" +
                "*********************************************");
        log.info("*********************** " + getRouteID() + " - ENDED ******************************\n\n");
    }
    
    private String getRouteID() {
        return request.getServletPath() + " [HTTP-METHOD=" + request.getMethod() + "]";
    }
    
    /**
     * builds the failure responses used in the api error handlers
     *
     * @return apiResponse
     */
    public AppApiResponse buildApiFailureResponse() {
        AppApiResponse apiResponse = (AppApiResponse) request.getAttribute(AppApiResponse.class.getSimpleName());
        apiResponse.setRequestSuccessful(false);
        return apiResponse;
    }
    
    
    /**
     * this should be called at the end of each api call to get the correct time
     *
     * @param apiResponse1
     * @return
     */
    public AppApiResponse arrangeFinalResponse(AppApiResponse apiResponse1) {
        log.debug("arrangeFinalResponse(ApiResponse apiResponse - in");
        
        if (watch.isRunning()) {
            watch.stop();
        }
        
        log.debug(watch.prettyPrint());
        
        apiResponse1.setExecutionTime(((double) (System.nanoTime() - startTime) / 1000000000));
        
        //wiretap here to log this in another thread if needed.
        log.debug(apiResponse1.toString());
        log.debug("arrangeFinalResponse(ApiResponse apiResponse, " +
                "final List<ApiError> apiErrorsList - out {}\n", apiResponse1);
        logRouteEnded();
        return apiResponse1;
    }
    
    private AppApiResponse prepareFinalResponse(AppApiResponse apiResponse, final List<AppApiError> apiErrorsList) {
        
        log.debug("prepareFinalResponse(ApiResponse apiResponse, final List<ApiError> apiErrorsList - in");
        log.debug(apiResponse.toString());
        log.debug("apiErrorsList - - {}", apiResponse.getApiErrors());
        
        if (apiResponse.getApiErrors() != null) {
            return apiResponse;
        }
        
        apiResponse.setApiErrors(new AppApiErrors(apiErrorsList.size(), apiErrorsList));
        
        log.debug("prepareFinalResponse(ApiResponse apiResponse, final List<ApiError> apiErrorsList - out");
        return apiResponse;
    }
    
    public AppApiResponse prepareValidationErrorResponse(String validationErrorCode, Object[] args, AppApiResponse apiResponse,
                                                         MessageSource messageSource) {
        
        String localizedErrorMessage = resolveLocalizedErrorMessage(validationErrorCode, args, messageSource);
        log.debug("localizedErrorMessage - {}", localizedErrorMessage);
        
        apiResponse.getApiErrors().getApiErrorList()
                .add(new AppApiError(validationErrorCode, localizedErrorMessage));
        
        return arrangeFinalResponse(apiResponse);
    }
    
    
    public String resolveLocalizedErrorMessage(String code, Object[] args, MessageSource messageSource) {
        Locale currentLocale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(code, args, currentLocale);
    }
    
    public AppApiResponse finalResponse(Object serviceResponse) {
        
        AppApiResponse apiResponse = this.arrangeSuccessResponse(this.arrangeInitialResponse());
        apiResponse.setResponseBody(serviceResponse);
        
        AppApiResponse appApiResponse = this.prepareFinalResponse(apiResponse, new ArrayList<>());
        
        return arrangeFinalResponse(appApiResponse);
    }
    
    /**
     * this is to format apiresponses returned from the service layer
     *
     * @param serviceLayerAppApiResponse
     * @return
     */
    public AppApiResponse buildFromServiceLayer(AppApiResponse serviceLayerAppApiResponse) {
        
        AppApiResponse apiResponse = this.arrangeSuccessResponse(this.arrangeInitialResponse());
        apiResponse.setResponseBody(serviceLayerAppApiResponse.getResponseBody());
        
        List<AppApiError> apiErrorList = serviceLayerAppApiResponse.getApiErrors().getApiErrorList();
        if (!CollectionUtils.isEmpty(apiErrorList)) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
        } else {
            response.setStatus(OK.value());
        }
        
        apiResponse.setApiErrors(null);
        
        AppApiResponse appApiResponse = this.prepareFinalResponse(apiResponse, apiErrorList);
        appApiResponse.setRequestSuccessful(serviceLayerAppApiResponse.isRequestSuccessful());
        
        return arrangeFinalResponse(appApiResponse);
    }
    
    
    /**
     * useful when returning a  specific node name to the api response
     *
     * @param nodeName
     * @param serviceResponse
     * @return
     */
    public AppApiResponse finalResponse(String nodeName, Object serviceResponse) {
        log.debug("finalResponse(ApiResponse apiResponse, ServiceResponse serviceResponse) {}", serviceResponse);
        
        Map<String, Object> returnedMap = new HashMap<>(1);
        returnedMap.put(nodeName, serviceResponse);
        return finalResponse(returnedMap);
    }
    
    private AppApiResponse arrangeSuccessResponse(AppApiResponse apiResponse) {
        
        apiResponse.setRequestSuccessful(true);
        return apiResponse;
    }
    
    /**
     * use when we have a pageable returned for the service layer
     *
     * @param collection
     * @return
     */
    public AppApiResponse entityPagedList(Page<?> collection) {

        AppApiResponse apiResponse = arrangeInitialResponse();
        if (!collection.hasContent()) {
            arrangeSuccessResponse(apiResponse);
            Pageable pageable = PageRequest.of(new PageDto().getPageNumber(), new PageDto().getPageSize(), Sort.by(
                    Sort.Order.desc("terminal")));
            ResponseEntity<?> responseEntity = new ResponseEntity<>(new PageImpl<>(new ArrayList<>(), pageable, 0), HttpStatus.valueOf(OK.value()));
            apiResponse.setResponseBody(responseEntity);
        } else {
            
            arrangeSuccessResponse(apiResponse);
            
            ResponseEntity<?> responseEntity = new ResponseEntity<>(collection, HttpStatus.valueOf(OK.value()));
            apiResponse.setResponseBody(responseEntity);
        }
        return arrangeFinalResponse(apiResponse);
        
    }
    
    public AppApiResponse entityList(List<?> collection) {
        
        AppApiResponse apiResponse = arrangeInitialResponse();
        
        if (collection.isEmpty()) {
            arrangeSuccessResponse(apiResponse);
            
            ResponseEntity<?> responseEntity = new ResponseEntity<>("paged list is empty", HttpStatus.valueOf(OK.value()));
            apiResponse.setResponseBody(responseEntity);
        } else {
            
            arrangeSuccessResponse(apiResponse);
            
            ResponseEntity<?> responseEntity = new ResponseEntity<>(collection, HttpStatus.valueOf(OK.value()));
            apiResponse.setResponseBody(responseEntity);
        }
        return arrangeFinalResponse(apiResponse);
        
    }
}
