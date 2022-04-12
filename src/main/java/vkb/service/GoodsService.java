package vkb.service;


import org.springframework.transaction.annotation.Transactional;
import vkb.controller.common.AppApiResponse;
import vkb.dto.GoodsRequestDto;
import vkb.dto.PageDto;


public interface GoodsService {
    AppApiResponse register(GoodsRequestDto goodsRequestDto);

    @Transactional(readOnly = true)
    AppApiResponse fetchAll(PageDto pageDto);
}
