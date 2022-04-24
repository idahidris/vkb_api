package vkb.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import vkb.controller.common.ApiResponseUtil;
import vkb.controller.common.AppApiResponse;
import vkb.dto.GoodsRequestDto;
import vkb.dto.PageDto;
import vkb.service.GoodsService;

import javax.validation.Valid;



@RestController
@RequestMapping(ApiController.BASE_PATH)
@Slf4j
@CrossOrigin
public class GoodsController extends ApiController {

    private final GoodsService goodsService;
    private final ApiResponseUtil apiResponseUtil;
    ObjectMapper objectMapper = new ObjectMapper();


    public GoodsController(GoodsService goodsService,
                           ApiResponseUtil apiResponseUtil) {

        this.goodsService = goodsService;
        this.apiResponseUtil = apiResponseUtil;
    }

    /**
     * api to get goods and return goods
     *
     * @param pageDto
     * @return
     */

    @GetMapping(value = GOODS)
    public AppApiResponse getGoods(@Valid @RequestBody PageDto pageDto) throws JsonProcessingException {


        log.info("************************** start get all terminal api **************************");
        log.info("REQUEST==>"+objectMapper.writeValueAsString(pageDto));
        AppApiResponse appApiResponse = goodsService.fetchAll(pageDto);
        log.info("RESPONSE==>"+objectMapper.writeValueAsString(appApiResponse));
        log.info("********************* finished executing get all terminal api *************************");

       return appApiResponse;
    }

    @GetMapping(value = GOODS+"/{goodsId}")
    public AppApiResponse getGoodsById(@Valid @PathVariable String goodsId) throws JsonProcessingException {


        log.info("************************** start get all terminal api **************************");
        log.info("Path Variable==>"+goodsId);
        AppApiResponse appApiResponse = goodsService.findById(goodsId);
        log.info("RESPONSE==>"+objectMapper.writeValueAsString(appApiResponse));
        log.info("********************* finished executing get all terminal api *************************");

        return appApiResponse;
    }

//
//    /**
//     * returns a single terminal using terminal ID
//     *
//     * @param terminalId
//     * @return
//     */
//    @Operation(summary = "Get a terminal details given the terminal Id")
//    @GetMapping(value = TERMINAL)
//    public AppApiResponse getTerminalByTerminalID(@RequestParam String terminalId) {
//
//        log.info("************************** start getTerminalByTerminalID api **************************");
//        AppApiResponse appApiResponse = terminalOperationService.findByTerminalId(terminalId);
//        log.info("********************* finished executing getTerminalByTerminalID api *************************");
//        return apiResponseUtil.buildFromServiceLayer(appApiResponse);
//    }

    /**
     * api to create bulk terminals

     * @param goodsRequestDto
     * @return
     */

    @PostMapping(value = GOODS)
    public AppApiResponse createGoods(@Valid @RequestBody GoodsRequestDto goodsRequestDto) throws JsonProcessingException {
        log.info("************************** start create goods api **************************");

        log.info("REQUEST==>"+objectMapper.writeValueAsString(goodsRequestDto));
        AppApiResponse appApiResponse
                = goodsService.register(goodsRequestDto);

        AppApiResponse response = apiResponseUtil.buildFromServiceLayer(appApiResponse);
        log.info("RESPONSE==>"+objectMapper.writeValueAsString(response));
        log.info("********************* finished executing create goods api *************************");
        return response;
    }

//    /**
//     * api to update bulk terminals
//
//     * @param terminalFileUploadBulkRequestDto
//     * @return
//     */
//    @Operation(summary = "Update list of terminals")
//    @PutMapping(value = TERMINALS)
//    public AppApiResponse updateTerminals(@Valid @RequestBody TerminalFileUploadBulkRequestDto terminalFileUploadBulkRequestDto) {
//        log.info("************************** start update terminals api **************************");
//        AppApiResponse appApiResponse
//                = terminalOperationService.updateBulkTerminal(terminalFileUploadBulkRequestDto);
//        log.info("********************* finished executing update terminals api *************************");
//        return apiResponseUtil.buildFromServiceLayer(appApiResponse);
//    }




}
