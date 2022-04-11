package vkb.util;

public class CommonUtil {

    public static String getCause(Throwable throwable){
        Throwable cause;
        Throwable result = throwable;
        while (null != (cause=throwable.getCause()) && (result != cause)){
            result = cause;
        }
        return result.getMessage();
    }

}
