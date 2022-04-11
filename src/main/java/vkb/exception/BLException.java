package vkb.exception;



public class BLException extends RuntimeException {

    public BLException() {
    }

    public BLException(String message) {
        super(message);
    }

    public BLException(String message, Throwable cause) {
        super(message, cause);
    }

    public BLException(Throwable cause) {
        super(cause);
    }

    public BLException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
