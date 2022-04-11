package vkb.exception;


public class UnauthorizedAccessException extends AbstractException {

    public UnauthorizedAccessException(String status, String message) {
        super(status, message);
    }
}