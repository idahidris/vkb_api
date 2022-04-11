package vkb.util;


public abstract class Constants {
    public static final String DUPLICATE_TERMINAL_GROUP = "duplicate terminal group id detected";
    public static final String EMPTY_RECORDS = "no record found";
    public static final String EMPTY_ROUTE_SETUP = "no processor setup for terminal(s)";
    public static final String SUCCESSFUL = "00";
    public static final String NOT_RECEIVED_RE_ADVISE = "02";
    public static final String FAILED = "99";
    public static final String REJECTED = "03";
    public static final String UNKNOWN_RESPONSE_CODE = "04";
    public static final String PARTIALLY_SUCCESSFUL = "05";
    public static final String READ_TIMED_OUT = "06";
    public static final String CONNECTION_TIMEOUT = "07";
    public static final String OTHER_RESOURCE_ACCESS = "08";
    public static final String ADVISE_ENDPOINT_SUFFIX = "/advise";
    public static final String STATUS_ENDPOINT_SUFFIX = "/status";
    public static final String CREATE_ENDPOINT_SUFFIX = "/create";
    public static final String SYSTEM_EXCEPTION = "96";
    public static final String NOT_PROCESSED = "09";
    public static final String NOT_PERMITTED_ERROR_MESSAGE = "not permitted, invalid %s code(s) for terminal:%s -> %s";
    public static final String NO_ERROR_OK = "";
    public static final String USER_NOT_SPECIFIED = "username not provided";




    private Constants() {
    }






}
