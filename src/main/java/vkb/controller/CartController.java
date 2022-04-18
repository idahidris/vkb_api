package vkb.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import vkb.controller.common.ApiResponseUtil;
import vkb.controller.common.AppApiResponse;
import vkb.service.CartService;

import javax.validation.Valid;


@RestController
@RequestMapping(ApiController.BASE_PATH)
@Slf4j
@CrossOrigin
public class CartController extends ApiController {

    private final CartService cartService;
    private final ApiResponseUtil apiResponseUtil;
    ObjectMapper objectMapper = new ObjectMapper();


    public CartController(CartService cartService,
                          ApiResponseUtil apiResponseUtil) {

        this.cartService = cartService;
        this.apiResponseUtil = apiResponseUtil;
    }



    @PostMapping(value = CART+"/{id}/{qty}")
    public AppApiResponse createCart(@Valid @PathVariable String id,  @Valid @PathVariable int qty) throws JsonProcessingException {
        log.info("************************** start create cart api **************************");

        log.info("REQUEST==> id:"+id +", qty:"+qty);
        AppApiResponse appApiResponse
                = cartService.add(id, qty);

        AppApiResponse response = apiResponseUtil.buildFromServiceLayer(appApiResponse);
        log.info("RESPONSE==>"+objectMapper.writeValueAsString(response));
        log.info("********************* finished executing create cart api *************************");
        return response;
    }

    @DeleteMapping(value = CART+"/{id}")
    public AppApiResponse deleteCart(@Valid @PathVariable String id) throws JsonProcessingException {
        log.info("************************** start delete cart api **************************");

        log.info("REQUEST==>"+id);
        AppApiResponse appApiResponse
                = cartService.remove(id);

        AppApiResponse response = apiResponseUtil.buildFromServiceLayer(appApiResponse);
        log.info("RESPONSE==>"+objectMapper.writeValueAsString(response));
        log.info("********************* finished executing delete cart api *************************");
        return response;
    }

    @PutMapping(value = CART+"/{user}/{customerRef}")
    public AppApiResponse cartToSales(@Valid @PathVariable String user, @PathVariable String customerRef) throws JsonProcessingException {
        log.info("************************** start patch cart api **************************");

        log.info("REQUEST==>"+user+", "+customerRef);
        AppApiResponse appApiResponse
                = cartService.cartToSales(user, customerRef);

        AppApiResponse response = apiResponseUtil.buildFromServiceLayer(appApiResponse);
        log.info("RESPONSE==>"+objectMapper.writeValueAsString(response));
        log.info("********************* finished executing patch cart api *************************");
        return response;
    }
//
//
//    @DeleteMapping(value = CART)
//    public AppApiResponse deleteAll(@Valid @RequestBody String userId) throws JsonProcessingException {
//        log.info("************************** start delete all cart api **************************");
//
//        log.info("REQUEST==>"+userId);
//        AppApiResponse appApiResponse
//                = cartService.removeAllByUserId(userId);
//
//        AppApiResponse response = apiResponseUtil.buildFromServiceLayer(appApiResponse);
//        log.info("RESPONSE==>"+objectMapper.writeValueAsString(response));
//        log.info("********************* finished executing delete all cart api *************************");
//        return response;
//    }
//
//
    @GetMapping(value = CART)
    public AppApiResponse findAll(@Valid @RequestBody String userId) throws JsonProcessingException {
        log.info("************************** start fetch all cart api **************************");

        log.info("REQUEST==>"+userId);
        AppApiResponse appApiResponse
                = cartService.fetchAllByUserId(userId);

        AppApiResponse response = apiResponseUtil.buildFromServiceLayer(appApiResponse);
        log.info("RESPONSE==>"+objectMapper.writeValueAsString(response));
        log.info("********************* finished executing fetch all cart api *************************");
        return response;
    }






}
