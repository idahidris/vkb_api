package vkb.service;


import vkb.controller.common.AppApiResponse;


public interface CartService {

    AppApiResponse add(String id, int qty);
    AppApiResponse remove(String id);
    AppApiResponse removeAllByUserId(String userId);
    AppApiResponse fetchAllByUserId(String userId);
    AppApiResponse cartToSales(String userId, String customerRef);

}
