package vkb.exception;


public class ServiceNotReachableException extends AbstractException {

    public ServiceNotReachableException(String status, String message) {
        super(status, message);
    }
}