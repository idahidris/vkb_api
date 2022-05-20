package vkb.util;

import vkb.controller.common.AppApiError;
import vkb.controller.common.AppApiErrors;
import vkb.controller.common.AppApiResponse;

import java.util.ArrayList;
import java.util.List;

public class CommonUtil {
    private static  final long LIMIT = 1000000000;
    private static long last =0;

    public static String getCause(Throwable throwable){
        Throwable cause;
        Throwable result = throwable;
        while (null != (cause=throwable.getCause()) && (result != cause)){
            result = cause;
        }
        return result.getMessage();
    }

    public static long getID(){
        long id = System.currentTimeMillis()% LIMIT;
        if(id<=last){
            id = (last+1)%LIMIT;
        }
        last = id;
        return id;
    }




}
