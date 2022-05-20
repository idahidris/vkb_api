package vkb.service;


import org.springframework.transaction.annotation.Transactional;
import vkb.controller.common.AppApiResponse;
import vkb.dto.GoodsRequestDto;
import vkb.dto.PageDto;
import vkb.entity.Goods;


public interface GoodsService {
    AppApiResponse register(GoodsRequestDto goodsRequestDto);

    @Transactional(readOnly = true)
    AppApiResponse fetchAll(PageDto pageDto);
    AppApiResponse findById(String id);
    AppApiResponse findByIdLike(String id);

    AppApiResponse update(Goods goods);
}
