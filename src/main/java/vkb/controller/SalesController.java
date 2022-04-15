//package vkb.controller;
//
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import io.swagger.v3.oas.annotations.Operation;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.web.bind.annotation.*;
//import vkb.controller.common.ApiResponseUtil;
//import vkb.controller.common.AppApiResponse;
//import vkb.dto.PageDto;
//import vkb.dto.SalesListRequestDto;
//import vkb.service.SalesService;
//
//import javax.validation.Valid;
//
//
//@RestController
//@RequestMapping(ApiController.BASE_PATH)
//@Slf4j
//@CrossOrigin
//public class SalesController extends ApiController {
//
//    private final SalesService salesService;
//    private final ApiResponseUtil apiResponseUtil;
//    ObjectMapper objectMapper = new ObjectMapper();
//
//
//    public SalesController(SalesService salesService,
//                           ApiResponseUtil apiResponseUtil) {
//
//        this.salesService = salesService;
//        this.apiResponseUtil = apiResponseUtil;
//    }
//
//    /**
//     * api to get goods and return goods
//     *
//     * @param pageDto
//     * @return
//     */
//    @Operation(summary = "Get all sales with pagination")
//    @GetMapping(value = SALES)
//    public AppApiResponse getGoods(@Valid @RequestBody PageDto pageDto) throws JsonProcessingException {
//
//
//        log.info("************************** start get all terminal api **************************");
//        log.info("REQUEST==>"+objectMapper.writeValueAsString(pageDto));
//        AppApiResponse appApiResponse = salesService.fetchAll(pageDto);
//        log.info("RESPONSE==>"+objectMapper.writeValueAsString(appApiResponse));
//        log.info("********************* finished executing get all terminal api *************************");
//
//       return appApiResponse;
//    }
//
//
//
//    /**
//     * api to create  sales
//
//     * @param salesListRequestDto
//     * @return
//     */
//    @Operation(summary = "Post sales")
//    @PostMapping(value = SALES)
//    public AppApiResponse createSales(@Valid @RequestBody SalesListRequestDto salesListRequestDto) throws JsonProcessingException {
//        log.info("************************** start create sales api **************************");
//
//        log.info("REQUEST==>"+objectMapper.writeValueAsString(salesListRequestDto));
//        AppApiResponse appApiResponse
//                = salesService.register(salesListRequestDto);
//
//        AppApiResponse response = apiResponseUtil.buildFromServiceLayer(appApiResponse);
//        log.info("RESPONSE==>"+objectMapper.writeValueAsString(response));
//        log.info("********************* finished executing create sales api *************************");
//        return response;
//    }
//
//
//
//
//
//
//}
