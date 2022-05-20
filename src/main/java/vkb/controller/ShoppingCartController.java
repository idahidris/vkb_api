package vkb.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import vkb.controller.common.ApiResponseUtil;
import vkb.controller.common.AppApiResponse;
import vkb.entity.ShoppingCart;
import vkb.service.ShoppingCartService;

import javax.validation.Valid;


@RestController
@RequestMapping(ApiController.BASE_PATH)
@Slf4j
@CrossOrigin
public class ShoppingCartController extends ApiController {

    private final ShoppingCartService cartService;
    private final ApiResponseUtil apiResponseUtil;
    ObjectMapper objectMapper = new ObjectMapper();


    public ShoppingCartController(ShoppingCartService cartService,
                                  ApiResponseUtil apiResponseUtil) {

        this.cartService = cartService;
        this.apiResponseUtil = apiResponseUtil;
    }



    @PostMapping(value = SHOPPING_CART)
    public AppApiResponse createCart(@Valid @RequestBody ShoppingCart shoppingCart) throws JsonProcessingException {
        log.info("************************** start create shopping cart api **************************");

        log.info("REQUEST==>"+objectMapper.writeValueAsString(shoppingCart));
        AppApiResponse appApiResponse
                = cartService.add(shoppingCart);

        AppApiResponse response = apiResponseUtil.buildFromServiceLayer(appApiResponse);
        log.info(" RESPONSE==>"+objectMapper.writeValueAsString(response));
        log.info("********************* finished create shopping cart api *************************");
        return response;
    }

    @DeleteMapping(value = SHOPPING_CART+"/{id}")
    public AppApiResponse deleteCart(@Valid @PathVariable String id) throws JsonProcessingException {
        log.info("************************** start delete shopping cart api **************************");

        log.info("REQUEST==>"+id);
        AppApiResponse appApiResponse
                = cartService.remove(id);

        AppApiResponse response = apiResponseUtil.buildFromServiceLayer(appApiResponse);
        log.info("RESPONSE==>"+objectMapper.writeValueAsString(response));
        log.info("********************* finished executing delete shopping cart api *************************");
        return response;
    }


    @GetMapping(value = SHOPPING_CART)
    public AppApiResponse findAll() throws JsonProcessingException {
        log.info("************************** start fetch all shopping cart api **************************");

        AppApiResponse appApiResponse
                = cartService.fetchAll();
        AppApiResponse response = apiResponseUtil.buildFromServiceLayer(appApiResponse);
        log.info("RESPONSE==>"+objectMapper.writeValueAsString(response));
        log.info("********************* finished executing fetch all shopping cart api *************************");
        return response;
    }






}
