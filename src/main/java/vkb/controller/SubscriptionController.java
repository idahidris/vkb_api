package vkb.controller;


import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vkb.controller.common.ApiResponseUtil;
import vkb.controller.common.AppApiResponse;
import vkb.dto.PageDto;
import vkb.entity.Subscription;
import vkb.service.SubscriptionService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.text.SimpleDateFormat;


@RestController
@RequestMapping(ApiController.BASE_PATH)
@Slf4j
@CrossOrigin
public class SubscriptionController extends ApiController {

    private final SubscriptionService subscriptionService;
    private final ApiResponseUtil apiResponseUtil;
    ObjectMapper objectMapper = new ObjectMapper().
            enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS).configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES,true).
            enable(SerializationFeature.INDENT_OUTPUT).
            setDateFormat(new SimpleDateFormat("dd-MM-yyyy")).
            configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).
            configure(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE, false);

    @Autowired
    private HttpServletRequest request;


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


    @PostMapping(value = SUBSCRIPTION, headers = ("content-Type=multipart/*"))
    public AppApiResponse createSubscription(@RequestParam(value = "file", required = false) MultipartFile file, @RequestParam("subscription") String subscriptionDto ) throws IOException {
        log.info("************************** start create subscription api **************************");
        Subscription subscription = objectMapper.readValue(subscriptionDto, Subscription.class);
        log.info("REQUEST ==>"+objectMapper.writeValueAsString(subscription));
        AppApiResponse appApiResponse
                = subscriptionService.register(subscription, file, ApiController.BASE_PATH+"downloadFile/");

        AppApiResponse response = apiResponseUtil.buildFromServiceLayer(appApiResponse);
        log.info("RESPONSE==>"+objectMapper.writeValueAsString(response));
       log.info("********************* finished executing create subscription api *************************");
        return response;

    }


    @PutMapping(value = SUBSCRIPTION, headers = ("content-Type=multipart/*"))
    public AppApiResponse updateSubscription(@RequestParam(value = "file", required = false) MultipartFile file, @RequestParam("subscription") String subscriptionDto ) throws JsonProcessingException {
        log.info("************************** start create subscription api **************************");

        Subscription subscription = objectMapper.readValue(subscriptionDto, Subscription.class);
        log.info("REQUEST==>"+objectMapper.writeValueAsString(subscription));
        AppApiResponse appApiResponse
                = subscriptionService.update(subscription, file, ApiController.BASE_PATH+"downloadFile/");

        AppApiResponse response = apiResponseUtil.buildFromServiceLayer(appApiResponse);
        log.info("RESPONSE==>"+objectMapper.writeValueAsString(response));
        log.info("********************* finished executing create subscription api *************************");
        return response;
    }






}
