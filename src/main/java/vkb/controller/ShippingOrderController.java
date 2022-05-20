package vkb.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import vkb.controller.common.ApiResponseUtil;
import vkb.controller.common.AppApiResponse;
import vkb.dto.PageDto;
import vkb.entity.ShippingOrder;
import vkb.service.ShippingOrderService;

import javax.validation.Valid;


@RestController
@RequestMapping(ApiController.BASE_PATH)
@Slf4j
@CrossOrigin
public class ShippingOrderController extends ApiController {

    private final ShippingOrderService shippingOrderService;
    ObjectMapper objectMapper = new ObjectMapper();
    private final ApiResponseUtil apiResponseUtil;



    public ShippingOrderController(ShippingOrderService shippingOrderService, ApiResponseUtil apiResponseUtil) {

        this.shippingOrderService = shippingOrderService;
        this.apiResponseUtil = apiResponseUtil;
    }

    /**
     * api to get all shipping orders and return shipping orders
     *
     * @param
     * @return
     */

    @GetMapping(value = SHOPPING_ORDER+"/{id}")
    public AppApiResponse getShippingOrdersById(@Valid @PathVariable String id) throws JsonProcessingException {



        log.info("************************** start get shipping order by id like api **************************");
        log.info("Path Variable==>"+id);
        AppApiResponse appApiResponse = shippingOrderService.findById(id);
        log.info("RESPONSE==> "+objectMapper.writeValueAsString(appApiResponse));
        log.info("********************* finished executing get shipping order by id like api *************************");

        return appApiResponse;
    }


    @GetMapping(value = SHOPPING_ORDER)
    public AppApiResponse getShippingOrders(@Valid @RequestBody PageDto pageDto) throws JsonProcessingException {


        log.info("************* start get all shipping orders api ****************");
        log.info("REQUEST==>"+objectMapper.writeValueAsString(pageDto));
        AppApiResponse appApiResponse = shippingOrderService.fetchAll(pageDto);
        log.info("RESPONSE==>"+objectMapper.writeValueAsString(appApiResponse));
        log.info("********** finished executing get all shipping orders api *******");

        return appApiResponse;
    }



    @PostMapping(value = SHOPPING_ORDER)
    public AppApiResponse registerShippingOrders(@Valid @RequestBody ShippingOrder shippingOrder) throws JsonProcessingException {

        log.info("************* start register shipping orders api ****************");
        log.info("REQUEST==>"+objectMapper.writeValueAsString(shippingOrder));
        AppApiResponse appApiResponse = shippingOrderService.register(shippingOrder);
        apiResponseUtil.buildFromServiceLayer(shippingOrderService.register(shippingOrder));
        log.info("RESPONSE==>"+objectMapper.writeValueAsString(appApiResponse));
        log.info("********** finished executing register shipping orders api *******");

        return appApiResponse;
    }





}
