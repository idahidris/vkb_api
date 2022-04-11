package vkb.service;


import vkb.controller.common.AppApiResponse;
import vkb.dto.GoodsRequestDto;


public interface GoodsService {
    AppApiResponse register(GoodsRequestDto goodsRequestDto);
}
