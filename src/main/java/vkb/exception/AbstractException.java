package vkb.exception;

/**
 * defines properties to be extended by all the exception classes
 **/

public class AbstractException extends RuntimeException {

    private final String code;
    private final String message;

    public AbstractException(String code, String message) {
        this.message = message;
        this.code = code;

    }

    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

}
