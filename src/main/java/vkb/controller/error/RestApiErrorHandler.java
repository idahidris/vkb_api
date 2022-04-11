package vkb.controller.error;

import com.fasterxml.jackson.databind.JsonMappingException;
import vkb.controller.common.ApiResponseUtil;
import vkb.controller.common.AppApiError;
import vkb.controller.common.AppApiResponse;
import vkb.exception.ResourceNotFoundException;
import vkb.exception.ServiceNotReachableException;
import kotlin.KotlinNullPointerException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.service.spi.ServiceException;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.text.ParseException;
import java.time.format.DateTimeParseException;
import java.util.*;

/**
 * kudos to http://www.petrikainulainen.net/programming/spring-framework/spring-from-the-trenches-adding-validation-to-a-rest-api/
 */
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestApiErrorHandler {
    
    private final ApiResponseUtil apiUtil;
    private final HttpServletResponse httpServletResponse;
    private final MessageSource messageSource;
    
    private final String genericMessage = "generic.message"; //pls use this rarely as the message from the jvm may not be i18n
    
    public RestApiErrorHandler(ApiResponseUtil apiUtil,
                               HttpServletResponse httpServletResponse, MessageSource messageSource) {
        this.apiUtil = apiUtil;
        this.httpServletResponse = httpServletResponse;
        this.messageSource = messageSource;
    }
    
    
    /**
     * This is going to handle any general exception that has not being handled
     *
     * @param ex
     * @return
     */
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public AppApiResponse handleGenericException(Exception ex) {
        
        log.error("a generic exception occurred ", ex);
        
        httpServletResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        
        errorLogger(ex);
        
        AppApiResponse apiResponse = apiUtil.arrangeInitialResponse();
        Object[] args = new Object[]{ex.getClass().getSimpleName()};
        return apiUtil.prepareValidationErrorResponse(genericMessage, args, apiResponse, messageSource);
    }


    @ResponseBody
    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public AppApiResponse dataIntegrityViolationExceptionnHandler(DataIntegrityViolationException ex) {
        log.error("a DataIntegrityViolationException occurred ", ex);

        httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());

        errorLogger(ex);

        AppApiResponse apiResponse = apiUtil.arrangeInitialResponse();
        Object[] args = new Object[]{ex.getClass().getSimpleName()};
        return apiUtil.prepareValidationErrorResponse(genericMessage, args, apiResponse, messageSource);
    }


    @ResponseBody
    @ExceptionHandler(ServiceNotReachableException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public AppApiResponse serviceNotReachableExceptionHandler(ServiceNotReachableException ex) {
        log.error("a ServiceNotReachable exception occurred ", ex);

        httpServletResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());

        AppApiResponse apiResponse = apiUtil.arrangeInitialResponse();

        Object[] args = new Object[]{ex.getClass().getSimpleName()};
        return apiUtil.prepareValidationErrorResponse(genericMessage, args, apiResponse, messageSource);
    }

    private AppApiResponse getAppApiResponse(Exception ex) {
        AppApiResponse appApiResponse = apiUtil.arrangeInitialResponse();

        String errorMessage = "Handling error: " +
                ex.getClass().getSimpleName() + ", " + ex.getMessage();
        log.error("Handling error: {}", errorMessage);
        return appApiResponse;
    }
    
    @ResponseBody
    @ExceptionHandler(NullPointerException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public AppApiResponse nullPointerExceptionHandler(NullPointerException ex) {
        log.error("a null pointer exception occurred ", ex);
        
        httpServletResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        
        errorLogger(ex);
        
        AppApiResponse apiResponse = apiUtil.arrangeInitialResponse();
        Object[] args = new Object[]{ex.getClass().getSimpleName()};
        return apiUtil.prepareValidationErrorResponse(genericMessage, args, apiResponse, messageSource);

    }
    
    
    @ResponseBody
    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public AppApiResponse noSuchElementExceptionHandler(NoSuchElementException ex) {
        log.error("a No Such Element Exception occurred ", ex);
        
        httpServletResponse.setStatus(HttpStatus.NOT_FOUND.value());
        
        errorLogger(ex);
        
        AppApiResponse apiResponse = apiUtil.arrangeInitialResponse();
        Object[] args = new Object[]{ex.getMessage()};
        return apiUtil.prepareValidationErrorResponse("no.such.message.exception", args, apiResponse, messageSource);
    }

    @ResponseBody
    @ExceptionHandler(KotlinNullPointerException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public AppApiResponse kotlinNullPointerExceptionHandler(KotlinNullPointerException ex) {

        log.error("a Kotlin Null Pointer Exception occurred ", ex);

        httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());

        errorLogger(ex);

        AppApiResponse apiResponse = apiUtil.arrangeInitialResponse();
        Object[] args = new Object[]{ex.getMessage()};
        return apiUtil.prepareValidationErrorResponse("search.exception", args, apiResponse, messageSource);
    }

    
    @ResponseBody
    @ExceptionHandler(ServiceException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public AppApiResponse resourceNotFoundExceptionHandler(ServiceException ex) {
        log.error("a Service Exception occurred ", ex);
        
        httpServletResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        
        errorLogger(ex);
        
        AppApiResponse apiResponse = apiUtil.arrangeInitialResponse();
        Object[] args = new Object[]{ex.getMessage()};
        return apiUtil.prepareValidationErrorResponse("service.exception", args, apiResponse, messageSource);
    }

    
    @ResponseBody
    @ExceptionHandler(InvalidDataAccessApiUsageException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public AppApiResponse invalidDataAccessApiUsageExceptionHandler(InvalidDataAccessApiUsageException ex) {
        log.error("a Invalid Data Access Api Usage Exception occurred ", ex);
        
        httpServletResponse.setStatus(HttpStatus.NOT_FOUND.value());
        
        errorLogger(ex);
        
        AppApiResponse apiResponse = apiUtil.arrangeInitialResponse();
        Object[] args = new Object[]{ex.getLocalizedMessage()};

        return apiUtil.prepareValidationErrorResponse("invalid.data.Access.api.usage.exception", args, apiResponse, messageSource);
    }


    @ResponseBody
    @ExceptionHandler(NoResultException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public AppApiResponse noResultExceptionHandler(NoResultException ex) {
        log.error("a NoResultException occurred ", ex);

        AppApiResponse appApiResponse = getAppApiResponse(ex);

        AppApiError appApiError = new AppApiError("", ex.getMessage());
        appApiResponse.getApiErrors().getApiErrorList().add(appApiError);
        appApiResponse = apiUtil.arrangeFinalResponse(appApiResponse);

        return appApiResponse;
    }


    @ResponseBody
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public AppApiResponse illegalArgumentExceptionHandler(IllegalArgumentException ex) {
        log.error("a illegal argument exception occurred ", ex);

        httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());

        errorLogger(ex);

        AppApiResponse apiResponse = apiUtil.arrangeInitialResponse();
        Object[] args = new Object[]{ex.getClass().getSimpleName()};
        return apiUtil.prepareValidationErrorResponse(genericMessage, args, apiResponse, messageSource);
    }
    @ResponseBody
    @ExceptionHandler(NumberFormatException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public AppApiResponse numberFormatExceptionHandler(NumberFormatException ex) {
        log.error("a numberFormatException occurred ", ex);
        
        httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        
        errorLogger(ex);
        
        AppApiResponse apiResponse = apiUtil.arrangeInitialResponse();
        Object[] args = new Object[]{ex.getClass().getSimpleName()};
        return apiUtil.prepareValidationErrorResponse(genericMessage, args, apiResponse, messageSource);
    }
    
    private void errorLogger(Exception ex) {
        log.info("Handling error: {}", ex.getClass().getSimpleName() + ", " + ex.getMessage());
    }
    
    @ResponseBody
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public AppApiResponse mySQLIntegrityConstraintViolationExceptionHandler(SQLIntegrityConstraintViolationException ex) {
        log.error("a null pointer exception occurred ", ex);
        
        httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        
        errorLogger(ex);
        
        AppApiResponse apiResponse = apiUtil.arrangeInitialResponse();
        Object[] args = new Object[]{ex.getClass().getSimpleName()};
        return apiUtil.prepareValidationErrorResponse(genericMessage, args, apiResponse, messageSource);
    }
    

    
    
    @ResponseBody
    @ExceptionHandler(DateTimeParseException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public AppApiResponse dateTimeParseExceptionHandler(DateTimeParseException ex) {
        log.error("an dateTime Parse Exception exception occurred ", ex);
        
        AppApiResponse appApiResponse = getAppApiResponse(ex);
        
        AppApiError appApiError = new AppApiError("", ex.getMessage());
        appApiResponse.getApiErrors().getApiErrorList().add(appApiError);
        appApiResponse = apiUtil.arrangeFinalResponse(appApiResponse);
        
        return appApiResponse;
    }
    


    
    @ResponseBody
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public AppApiResponse resourceNotFoundExceptionHandler(ResourceNotFoundException ex) {
        log.error("a ResourceNotFound exception occurred ", ex);
        
        AppApiResponse appApiResponse = getAppApiResponse(ex);
        AppApiError appApiError = new AppApiError("", ex.getMessage());
        appApiResponse.getApiErrors().getApiErrorList().add(appApiError);
        appApiResponse = apiUtil.arrangeFinalResponse(appApiResponse);
        
        return appApiResponse;
    }


    
    
    @ResponseBody
    @ExceptionHandler(ParseException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public AppApiResponse parseExceptionHandler(ParseException ex) {
        log.error("an Parse Exception exception occurred ", ex);
        
        AppApiResponse appApiResponse = getAppApiResponse(ex);
        
        
        AppApiError appApiError = new AppApiError("", ex.getMessage());
        appApiResponse.getApiErrors().getApiErrorList().add(appApiError);
        appApiResponse = apiUtil.arrangeFinalResponse(appApiResponse);
        
        return appApiResponse;
    }
    
    @ResponseBody
    @ExceptionHandler(NonUniqueResultException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public AppApiResponse nonUniqueResultExceptionHandler(NonUniqueResultException ex) {
        log.error("an Non Unique Result Exception occurred ", ex);
        
        AppApiResponse appApiResponse = getAppApiResponse(ex);
        
        AppApiError appApiError = new AppApiError("", ex.getMessage());
        appApiResponse.getApiErrors().getApiErrorList().add(appApiError);
        appApiResponse = apiUtil.arrangeFinalResponse(appApiResponse);
        
        return appApiResponse;
    }
    
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public AppApiResponse handleNoHandlerFoundException(NoHandlerFoundException ex) {
        errorLogger(ex);
        
        httpServletResponse.setStatus(HttpStatus.NOT_FOUND.value());
        
        AppApiResponse apiResponse = apiUtil.arrangeInitialResponse();
        
        Object[] args = new Object[]{ex.getHttpMethod(), ex.getRequestURL()};
        
        String handlerNotFoundCode = "handler.not.found";
        
        return apiUtil.prepareValidationErrorResponse(handlerNotFoundCode, args, apiResponse, messageSource);
    }
    
    
    @ResponseBody
    @ExceptionHandler(IncorrectResultSizeDataAccessException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public AppApiResponse inCorrectResultSizeDataAccessExceptionHandler(IncorrectResultSizeDataAccessException ex) {
        log.error("an Incorrect Result Size DataAccessException exception occurred ", ex);
        
        AppApiResponse appApiResponse = getAppApiResponse(ex);
        
        
        AppApiError appApiError = new AppApiError("", ex.getMessage());
        appApiResponse.getApiErrors().getApiErrorList().add(appApiError);
        appApiResponse = apiUtil.arrangeFinalResponse(appApiResponse);
        
        return appApiResponse;
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public AppApiResponse processValidationError(MethodArgumentNotValidException ex) {
        
        log.error(" getBindingResult - {}, getMessage - {}, getParameter - {}, getFieldError - {}",
                ex.getBindingResult(), ex.getMessage(), ex.getParameter(), ex.getBindingResult().getFieldError());
        log.error("");
        
        httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        
        AppApiResponse apiResponse = apiUtil.arrangeInitialResponse();
        
        BindingResult result = ex.getBindingResult();
        
        List<FieldError> fieldErrors = result.getFieldErrors();
        
        for (FieldError fieldError : fieldErrors) {
            
            String localizedErrorMessage = resolveLocalizedErrorMessage(fieldError);

            apiResponse.getApiErrors().getApiErrorList().add(new AppApiError(fieldError.getField(), localizedErrorMessage));
            apiResponse.getApiErrors().setErrorCount(result.getErrorCount()); //
        }
        
        String errMsg = "Validation failed. " + result.getErrorCount() + " error(s)";
        
        
        ResponseEntity<?> responseEntity =
                new ResponseEntity<>(errMsg,
                        HttpStatus.valueOf(httpServletResponse.getStatus()));
        apiResponse.setResponseBody(responseEntity);
        
        return apiUtil.arrangeFinalResponse(apiResponse);
    }
    
    
    @ResponseBody
    @ExceptionHandler({ConstraintViolationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public AppApiResponse constraintViolationExceptionHandler(ConstraintViolationException ex) {
        
        log.error("\n\n *********************** " +
                "constraintViolationExceptionHandler(ConstraintViolationException ex)");
        
        AppApiResponse apiResponse = apiUtil.arrangeInitialResponse();
        final Set<ConstraintViolation<?>> constraintViolations = ex.getConstraintViolations();
        for (ConstraintViolation<?> constraintViolation : constraintViolations) {
            String localizedErrorMessage = resolveLocalizedErrorMessage(constraintViolation);
            apiResponse.getApiErrors().getApiErrorList().add(new AppApiError("", localizedErrorMessage));
        }
        
        String errMsg = "Validation failed. " + ex.getConstraintViolations().size() + " error(s)";
        httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        
        ResponseEntity<?> responseEntity =
                new ResponseEntity<>(errMsg,
                        HttpStatus.valueOf(httpServletResponse.getStatus()));
        apiResponse.setResponseBody(responseEntity);
        return apiUtil.arrangeFinalResponse(apiResponse);
    }
    
    
    @ResponseBody
    @ExceptionHandler({HttpMessageNotReadableException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    AppApiResponse httpMessageNotReadableExceptionHandler(HttpMessageNotReadableException ex) {
        
        errorLogger(ex);
        
        AppApiResponse apiResponse = apiUtil.arrangeInitialResponse();
        
        Object[] args = new Object[]{Objects.requireNonNull(ex.getMessage()).substring(0, 33), null};
        
        
        if (ex.getCause() instanceof JsonMappingException) {
            JsonMappingException jme = (JsonMappingException) ex.getCause();
            List<JsonMappingException.Reference> path = jme.getPath();
            if (!path.isEmpty()) {
                log.error(path.get(0).getFieldName() + " - invalid");
                args = new Object[]{path.get(0).getFieldName() + " - invalid", null};
            }
            
        } else {
            log.info("Handling getMostSpecificCause: " + ex.getMostSpecificCause());
        }
        httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        return apiUtil.prepareValidationErrorResponse("http.message.not.readable.exception", args, apiResponse, messageSource);
    }
    
    
    private String resolveLocalizedErrorMessage(FieldError fieldError) {
        
        log.debug("FieldError fieldError-getArguments {}", fieldError.getArguments());
        log.debug("FieldError fieldError.getField {}", fieldError.getField());
        log.debug("FieldError fieldError.getRejectedValue {}", fieldError.getRejectedValue());
        log.debug("FieldError fieldError.getObjectName {}", fieldError.getObjectName());
        log.debug("FieldError fieldError.getCode {}", fieldError.getCode());
        log.debug("FieldError fieldError.getDefaultMessage {}", fieldError.getDefaultMessage());
        log.debug("FieldError fieldError.getObjectName {}", fieldError.getObjectName());
        
        for (String s : Objects.requireNonNull(fieldError.getCodes())) {
            log.error("fieldError.getCode - {}", s);
        }
        String defaultMessage =
                fieldError.getDefaultMessage();
        
        
        log.debug("defaultMessage {}", defaultMessage);
        
        String localizedErrorMessage = null;
        try {
            
            localizedErrorMessage = defaultMessage;
            
        } catch (NoSuchMessageException noSuchMessageException) {
            log.error("-------- FATAL NoSuchMessageException PLS FIX --------");
            log.error("NoSuchMessageException - ", noSuchMessageException);
            log.error("-------- FATAL NoSuchMessageException PLS FIX --------");
        }
        log.debug("localizedErrorMessage {}", localizedErrorMessage);
        
        
        return localizedErrorMessage;
    }
    
    private String resolveLocalizedErrorMessage(ConstraintViolation constraintViolation) {
        
        List<Object> args = new ArrayList<>();
        final Map<String, Object> attributes = constraintViolation.getConstraintDescriptor().getAttributes();
        
        if (constraintViolation.getConstraintDescriptor()
                .getAnnotation().annotationType().getSimpleName().equalsIgnoreCase("Size")) {
            
            log.debug("this is a @Size annotation");
            
            
            if (attributes.containsKey("min")) {
                args.add(attributes.get("min"));
            }
            if (attributes.containsKey("max")) {
                args.add(attributes.get("max"));
            }
            log.info("args {}", args);
            return apiUtil.resolveLocalizedErrorMessage(constraintViolation.getMessageTemplate(), args.toArray(), messageSource);
        }
        if (constraintViolation.getConstraintDescriptor()
                .getAnnotation().annotationType().getSimpleName().equalsIgnoreCase("NotEmpty")) {
            log.debug("this is a @NotEmpty annotation");
            return apiUtil.resolveLocalizedErrorMessage(constraintViolation.getMessageTemplate(),
                    Collections.emptyList().toArray(), messageSource);
            
        }
        if (constraintViolation.getConstraintDescriptor()
                .getAnnotation().annotationType().getSimpleName().equalsIgnoreCase("Min")
                || constraintViolation.getConstraintDescriptor()
                .getAnnotation().annotationType().getSimpleName().equalsIgnoreCase("Max")) {
            
            log.debug("this is a @Min or @Max annotation");
            if (attributes.containsKey("value")) {
                args.add(attributes.get("value"));
            }
            return apiUtil.resolveLocalizedErrorMessage(constraintViolation.getMessageTemplate(), args.toArray(), messageSource);
            
        }
        log.debug("this is NOT a @Size annotation");
        return apiUtil.resolveLocalizedErrorMessage(constraintViolation.getMessage(), new Object[]{}, messageSource);
        
        
    }
}
