package vkb.service;


import vkb.controller.common.AppApiResponse;
import vkb.entity.ShoppingCart;


public interface ShoppingCartService {

    AppApiResponse add(ShoppingCart shoppingCart);
    AppApiResponse remove(String id);
    AppApiResponse fetchAll();
    //AppApiResponse cartToOrder(String userId, String customerRef);

}
