package vkb.service;


import vkb.controller.common.AppApiResponse;
import vkb.dto.CartDto;


public interface CartService {

    AppApiResponse add(String id, int qty);
    AppApiResponse remove(CartDto cartDto);
    AppApiResponse removeAllByUserId(String userId);
    AppApiResponse fetchAllByUserId(String userId);

}
