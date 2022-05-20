package vkb.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import vkb.controller.common.ApiResponseUtil;
import vkb.controller.common.AppApiResponse;
import vkb.dto.GoodsRequestDto;
import vkb.dto.PageDto;
import vkb.entity.Goods;
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


        log.info("************************** start get all goods api **************************");
        log.info("REQUEST==>"+objectMapper.writeValueAsString(pageDto));
        AppApiResponse appApiResponse = goodsService.fetchAll(pageDto);
        log.info("RESPONSE==>"+objectMapper.writeValueAsString(appApiResponse));
        log.info("********************* finished executing get all goods api *************************");

       return appApiResponse;
    }

    @GetMapping(value = GOODS+"/{goodsId}")
    public AppApiResponse getGoodsByIdLike(@Valid @PathVariable String goodsId) throws JsonProcessingException {


        log.info("************************** start get goods by id like api **************************");
        log.info("Path Variable==>"+goodsId);
        AppApiResponse appApiResponse = goodsService.findByIdLike(goodsId);
        log.info("RESPONSE==>"+objectMapper.writeValueAsString(appApiResponse));
        log.info("********************* finished executing get goods by id like api *************************");

        return appApiResponse;
    }


    @GetMapping(value = GOODS+"/id/{goodsId}")
    public AppApiResponse getGoodsById(@Valid @PathVariable String goodsId) throws JsonProcessingException {


        log.info("************************** start get goods by id api **************************");
        log.info("Path Variable==>"+goodsId);
        AppApiResponse appApiResponse = goodsService.findById(goodsId);
        log.info("RESPONSE==>"+objectMapper.writeValueAsString(appApiResponse));
        log.info("********************* finished executing get goods by id api *************************");

        return appApiResponse;
    }



    /**
     * api to create goods

     * @param goodsRequestDto
     * @return
     */

    @PostMapping(value = GOODS)
    public AppApiResponse createGoods(@Valid @RequestBody GoodsRequestDto goodsRequestDto) throws JsonProcessingException {
        log.info("******************* start register goods api *******************");

        log.info("REQUEST==>"+objectMapper.writeValueAsString(goodsRequestDto));
        AppApiResponse appApiResponse
                = goodsService.register(goodsRequestDto);

        AppApiResponse response = apiResponseUtil.buildFromServiceLayer(appApiResponse);
        log.info("RESPONSE==>"+objectMapper.writeValueAsString(response));
        log.info("************ finished executing register goods api ************");
        return response;
    }


    @PutMapping(value = GOODS)
    public AppApiResponse updateGoods(@Valid @RequestBody Goods goods) throws JsonProcessingException {
        log.info("************************** start create goods api **************************");

        log.info("REQUEST==>"+objectMapper.writeValueAsString(goods));
        AppApiResponse appApiResponse
                = goodsService.update(goods);

        AppApiResponse response = apiResponseUtil.buildFromServiceLayer(appApiResponse);
        log.info("RESPONSE==>"+objectMapper.writeValueAsString(response));
        log.info("********************* finished executing create goods api *************************");
        return response;
    }




}
