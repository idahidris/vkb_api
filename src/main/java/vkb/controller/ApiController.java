package vkb.controller;


import org.springframework.web.bind.annotation.CrossOrigin;

/**
 * @author :osatoojo @email : oosato@nibss-plc.com.ng,    @date ::16/09/2019
 * <p>
 * all the api paths used in the controllers are defined here
 */
@CrossOrigin
public abstract class ApiController {


    static final String BASE_PATH = "/api/v1/";
    static final String AUTH_BASE_PATH = "/api/auth/";
    static final String GOODS = "goods";
    static final String USER = "user";
    static final String SALES = "sales";
    static final String REPORT = "report";
    static final String CART = "cart";
    static final String SHOPPING_CART = "shoppingCart";
    static final String SHOPPING_ORDER = "shipping-order";
    static final String SUBSCRIPTION = "subscription";
    static final String DOWNLOAD_FILE = "downloadFile";
    static final String SIGN_IN = "signin";
    static final String SIGN_UP = "signup";
    static final String LOGOUT = "logout";
    static final String REFRESH_TOKEN = "refreshtoken";


    
    
}
