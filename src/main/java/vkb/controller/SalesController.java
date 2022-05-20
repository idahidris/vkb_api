package vkb.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import vkb.controller.common.AppApiResponse;
import vkb.dto.PageDto;
import vkb.service.SalesService;

import javax.validation.Valid;


@RestController
@RequestMapping(ApiController.BASE_PATH)
@Slf4j
@CrossOrigin
public class SalesController extends ApiController {

    private final SalesService salesService;
    ObjectMapper objectMapper = new ObjectMapper();



    public SalesController(SalesService salesService) {

        this.salesService = salesService;
    }

    /**
     * api to get goods and return goods
     *
     * @param pageDto
     * @return
     */

    @GetMapping(value = SALES)
    public AppApiResponse getSales(@Valid @RequestBody PageDto pageDto) throws JsonProcessingException {


        log.info("************************** start get all sales api **************************");
        log.info("REQUEST==>"+objectMapper.writeValueAsString(pageDto));
        AppApiResponse appApiResponse = salesService.fetchAll(pageDto);
        log.info("RESPONSE==>"+objectMapper.writeValueAsString(appApiResponse));
        log.info("********************* finished executing get all sales api *************************");

       return appApiResponse;
    }





}
