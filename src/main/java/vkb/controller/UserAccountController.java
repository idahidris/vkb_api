package vkb.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import vkb.controller.common.ApiResponseUtil;
import vkb.controller.common.AppApiResponse;
import vkb.dto.PageDto;
import vkb.entity.UserAccount;
import vkb.service.UserAccountService;

import javax.validation.Valid;


@RestController
@RequestMapping(ApiController.BASE_PATH)
@Slf4j
@CrossOrigin
public class UserAccountController extends ApiController {

    private final UserAccountService userAccountService;
    private final ApiResponseUtil apiResponseUtil;
    ObjectMapper objectMapper = new ObjectMapper();


    public UserAccountController(UserAccountService userAccountService,
                                 ApiResponseUtil apiResponseUtil) {

        this.userAccountService = userAccountService;
        this.apiResponseUtil = apiResponseUtil;
    }


    @GetMapping(value = USER)
    public AppApiResponse getUserAccounts(@Valid @RequestBody PageDto pageDto) throws JsonProcessingException {


        log.info("************************** start get all terminal api **************************");
        log.info("REQUEST==>"+objectMapper.writeValueAsString(pageDto));
        AppApiResponse appApiResponse = userAccountService.fetchAll(pageDto);
        log.info("RESPONSE==>"+objectMapper.writeValueAsString(appApiResponse));
        log.info("********************* finished executing get all terminal api *************************");

       return appApiResponse;
    }


    @GetMapping(value = USER+"/id/{id}")
    public AppApiResponse getUserAccountById(@Valid @PathVariable String id) throws JsonProcessingException {


        log.info("************************** start get user by id api **************************");
        log.info("Path Variable==>"+id);
        AppApiResponse appApiResponse = userAccountService.findById(id);
        log.info("RESPONSE==>"+objectMapper.writeValueAsString(appApiResponse));
        log.info("********************* finished executing get user by id api *************************");

        return appApiResponse;
    }

    @GetMapping(value = USER+"/{firstName}")
    public AppApiResponse getUserAccount(@Valid @PathVariable String firstName) throws JsonProcessingException {


        log.info("************************** start get get user by firstName api **************************");
        log.info("Path Variable==>"+firstName);
        AppApiResponse appApiResponse = userAccountService.findByFirstName(firstName);
        log.info("RESPONSE==>"+objectMapper.writeValueAsString(appApiResponse));
        log.info("********************* finished executing get user by firstName api *************************");

        return appApiResponse;
    }




    @PostMapping(value = USER)
    public AppApiResponse createUserAccount(@Valid @RequestBody UserAccount userAccount) throws JsonProcessingException {
        log.info("************************** start create goods api **************************");

        log.info("REQUEST==>"+objectMapper.writeValueAsString(userAccount));
        AppApiResponse appApiResponse
                = userAccountService.register(userAccount);

        AppApiResponse response = apiResponseUtil.buildFromServiceLayer(appApiResponse);
        log.info("RESPONSE==>"+objectMapper.writeValueAsString(response));
        log.info("********************* finished executing create goods api *************************");
        return response;
    }



    @PutMapping(value = USER)
    public AppApiResponse updateUserAccount(@Valid @RequestBody UserAccount userAccount) throws JsonProcessingException {
        log.info("************************** start create goods api **************************");

        log.info("REQUEST==>"+objectMapper.writeValueAsString(userAccount));
        AppApiResponse appApiResponse
                = userAccountService.update(userAccount);

        AppApiResponse response = apiResponseUtil.buildFromServiceLayer(appApiResponse);
        log.info("RESPONSE==>"+objectMapper.writeValueAsString(response));
        log.info("********************* finished executing create goods api *************************");
        return response;
    }






}
