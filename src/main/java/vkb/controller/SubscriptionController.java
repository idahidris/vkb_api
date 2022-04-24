package vkb.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import vkb.controller.common.ApiResponseUtil;
import vkb.controller.common.AppApiResponse;
import vkb.dto.PageDto;
import vkb.entity.Subscription;
import vkb.service.SubscriptionService;

import javax.validation.Valid;


@RestController
@RequestMapping(ApiController.BASE_PATH)
@Slf4j
@CrossOrigin
public class SubscriptionController extends ApiController {

    private final SubscriptionService subscriptionService;
    private final ApiResponseUtil apiResponseUtil;
    ObjectMapper objectMapper = new ObjectMapper();


    public SubscriptionController(SubscriptionService subscriptionService,
                                  ApiResponseUtil apiResponseUtil) {

        this.subscriptionService = subscriptionService;
        this.apiResponseUtil = apiResponseUtil;
    }


    @GetMapping(value = SUBSCRIPTION)
    public AppApiResponse getSubscriptions(@Valid @RequestBody PageDto pageDto) throws JsonProcessingException {


        log.info("************************** start get all subscription api **************************");
        log.info("REQUEST==>"+objectMapper.writeValueAsString(pageDto));
        AppApiResponse appApiResponse = subscriptionService.fetchAll(pageDto);
        log.info("RESPONSE==>"+objectMapper.writeValueAsString(appApiResponse));
        log.info("********************* finished executing get all subscription api *************************");

       return appApiResponse;
    }



    @GetMapping(value = SUBSCRIPTION+"/{id}")
    public AppApiResponse getSubscriptionById(@Valid @PathVariable String id) throws JsonProcessingException {


        log.info("************************** start get a subscription api **************************");
        log.info("Path Variable==>"+id);
        AppApiResponse appApiResponse = subscriptionService.findById(id);
        log.info("RESPONSE==>"+objectMapper.writeValueAsString(appApiResponse));
        log.info("********************* finished executing get a subscription api *************************");

        return appApiResponse;
    }





    @PostMapping(value = SUBSCRIPTION)
    public AppApiResponse createSubscription(@Valid @RequestBody Subscription subscription) throws JsonProcessingException {
        log.info("************************** start create subscription api **************************");

        log.info("REQUEST==>"+objectMapper.writeValueAsString(subscription));
        AppApiResponse appApiResponse
                = subscriptionService.register(subscription);

        AppApiResponse response = apiResponseUtil.buildFromServiceLayer(appApiResponse);
        log.info("RESPONSE==>"+objectMapper.writeValueAsString(response));
        log.info("********************* finished executing create subscription api *************************");
        return response;
    }


    @PutMapping(value = SUBSCRIPTION)
    public AppApiResponse updateSubscription(@Valid @RequestBody Subscription subscription) throws JsonProcessingException {
        log.info("************************** start create subscription api **************************");

        log.info("REQUEST==>"+objectMapper.writeValueAsString(subscription));
        AppApiResponse appApiResponse
                = subscriptionService.update(subscription);

        AppApiResponse response = apiResponseUtil.buildFromServiceLayer(appApiResponse);
        log.info("RESPONSE==>"+objectMapper.writeValueAsString(response));
        log.info("********************* finished executing create subscription api *************************");
        return response;
    }






}
