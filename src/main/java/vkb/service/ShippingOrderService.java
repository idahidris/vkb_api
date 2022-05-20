package vkb.service;


import org.springframework.transaction.annotation.Transactional;
import vkb.controller.common.AppApiResponse;
import vkb.dto.PageDto;
import vkb.entity.ShippingOrder;


public interface ShippingOrderService {

    @Transactional(readOnly = true)
    AppApiResponse fetchAll(PageDto pageDto);

    AppApiResponse register(ShippingOrder shippingOrder);
    AppApiResponse findById(String id);

}
