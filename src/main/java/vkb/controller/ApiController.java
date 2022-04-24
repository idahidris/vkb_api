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

    static final String TERMINAL = "terminal";

    static final String TERMINALS = "terminals";

    static final String APIGATEWAYUSER = "api-gateway-user";

    static final String APIGATEWAYUSERS = "api-gateway-users";

    static final String GOODS = "goods";
    static final String USER = "user";
    static final String SALES = "sales";
    static final String REPORT = "report";
    static final String CART = "cart";
    static final String SUBSCRIPTION = "subscription";

    
    
}
