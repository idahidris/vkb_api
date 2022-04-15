package vkb.service.impl;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vkb.controller.common.ApiResponseUtil;
import vkb.controller.common.AppApiError;
import vkb.controller.common.AppApiErrors;
import vkb.controller.common.AppApiResponse;
import vkb.dto.CartDto;
import vkb.entity.Cart;
import vkb.entity.Goods;
import vkb.repository.CartRepository;
import vkb.repository.GoodsRepository;
import vkb.service.CartService;

import java.util.*;


@Service
@Slf4j
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final GoodsRepository goodsRepository;
    private final ApiResponseUtil apiResponseUtil;

    public CartServiceImpl(CartRepository cartRepository, GoodsRepository goodsRepository, ApiResponseUtil apiResponseUtil) {
        this.cartRepository = cartRepository;
        this.goodsRepository = goodsRepository;
        this.apiResponseUtil = apiResponseUtil;

    }


    @Override
    public AppApiResponse add(String id, int qty) {
        AppApiResponse appApiResponse = new AppApiResponse();

        try{
             Goods goods = goodsRepository.findById(id).orElse(null);

             if(qty<1) {
                AppApiError appApiError = new AppApiError("01", "invalid qty provided" + id);
                AppApiErrors appApiErrors = new AppApiErrors();
                List<AppApiError> listErrors = new ArrayList<>();
                listErrors.add(appApiError);
                appApiErrors.setApiErrorList(listErrors);
                appApiErrors.setErrorCount(1);
                appApiResponse.setApiErrors(appApiErrors);
            }

            else if(goods==null){

                AppApiError appApiError = new AppApiError("02", "Item not available==>"+id);
                AppApiErrors appApiErrors = new AppApiErrors();
                List<AppApiError> listErrors = new ArrayList<>();
                listErrors.add(appApiError);
                appApiErrors.setApiErrorList(listErrors);
                appApiErrors.setErrorCount(1);
                appApiResponse.setApiErrors(appApiErrors);

            }


            else {

                 Cart cart = cartRepository.findById(goods.getId()).orElse(null);
                 int prevQty = 0;
                if(Objects.isNull(cart)) {
                     cart = new Cart();
                    cart.setUserId("123456");
                    cart.setItemId(goods.getId());
                    cart.setQuantity(qty + "");
                    cart.setStoreName("AEKINS");
                    cart.setCreatedDate(new Date().toLocaleString());
                }
                else{
                     prevQty = Integer.parseInt(cart.getQuantity());
                    cart.setQuantity(qty+"");
                }

                //prev 5
                 //qty 6
                 //5 - 6
                 //-1
                int balance = qty - prevQty;

                int newGoodsBalance = Integer.parseInt(goods.getQuantity() ) - balance;

                if(newGoodsBalance<0){
                    AppApiError appApiError = new AppApiError("03", " require more "+ Math.abs(newGoodsBalance)+ " item, to meet up with your request:"+ qty);
                    AppApiErrors appApiErrors = new AppApiErrors();
                    List<AppApiError> listErrors = new ArrayList<>();
                    listErrors.add(appApiError);
                    appApiErrors.setApiErrorList(listErrors);
                    appApiErrors.setErrorCount(1);
                    appApiResponse.setApiErrors(appApiErrors);
                    return appApiResponse;

                }



                Map<String, Object> result = new HashMap<>();
                result.put("data",cartRepository.save(cart));
                result.put("remainder",newGoodsBalance);

                appApiResponse.setResponseBody(result);
                AppApiErrors appApiErrors = new AppApiErrors();
                List<AppApiError> listErrors = new ArrayList<>();
                appApiErrors.setApiErrorList(listErrors);
                appApiErrors.setErrorCount(0);
                appApiResponse.setApiErrors(appApiErrors);
                appApiResponse.setRequestSuccessful(true);

                goods.setQuantity(newGoodsBalance+"");
                goodsRepository.save(goods);

            }

        }
        catch (Exception ex){
            log.error(ex.getMessage());
            AppApiError appApiError = new AppApiError("04", "Please try again, later");
            AppApiErrors appApiErrors = new AppApiErrors();
            List<AppApiError> listErrors = new ArrayList<>();
            listErrors.add(appApiError);
            appApiErrors.setApiErrorList(listErrors);
            appApiErrors.setErrorCount(1);
            appApiResponse.setApiErrors(appApiErrors);

        }

        return appApiResponse;
    }

    @Override
    public AppApiResponse remove(CartDto cartDto) {
        AppApiResponse appApiResponse = new AppApiResponse();

        try{
            cartRepository.delete(cartDto.toCart());
            appApiResponse.setResponseBody(cartDto.toCart());
            AppApiErrors appApiErrors = new AppApiErrors();
            List<AppApiError> listErrors = new ArrayList<>();
            appApiErrors.setApiErrorList(listErrors);
            appApiErrors.setErrorCount(0);
            appApiResponse.setApiErrors(appApiErrors);
            appApiResponse.setRequestSuccessful(true);

        }
        catch (Exception ex){
            log.error(ex.getMessage());
            AppApiError appApiError = new AppApiError("02", "Please try again, later");
            AppApiErrors appApiErrors = new AppApiErrors();
            List<AppApiError> listErrors = new ArrayList<>();
            listErrors.add(appApiError);
            appApiErrors.setApiErrorList(listErrors);
            appApiErrors.setErrorCount(1);
            appApiResponse.setApiErrors(appApiErrors);

        }

        return appApiResponse;
    }

    @Override
    public AppApiResponse removeAllByUserId(String userId) {


        AppApiResponse appApiResponse = new AppApiResponse();

        try{
            cartRepository.deleteAllByUserId(userId);
            appApiResponse.setResponseBody(null);
            AppApiErrors appApiErrors = new AppApiErrors();
            List<AppApiError> listErrors = new ArrayList<>();
            appApiErrors.setApiErrorList(listErrors);
            appApiErrors.setErrorCount(0);
            appApiResponse.setApiErrors(appApiErrors);
            appApiResponse.setRequestSuccessful(true);

        }
        catch (Exception ex){
            log.error(ex.getMessage());
            AppApiError appApiError = new AppApiError("02", "Please try again, later");
            AppApiErrors appApiErrors = new AppApiErrors();
            List<AppApiError> listErrors = new ArrayList<>();
            listErrors.add(appApiError);
            appApiErrors.setApiErrorList(listErrors);
            appApiErrors.setErrorCount(1);
            appApiResponse.setApiErrors(appApiErrors);

        }

        return appApiResponse;
    }

    @Override
    public AppApiResponse fetchAllByUserId(String userId) {


        AppApiResponse appApiResponse = new AppApiResponse();

        try{
            appApiResponse.setResponseBody(cartRepository.findAllByUserId(userId));
            AppApiErrors appApiErrors = new AppApiErrors();
            List<AppApiError> listErrors = new ArrayList<>();
            appApiErrors.setApiErrorList(listErrors);
            appApiErrors.setErrorCount(0);
            appApiResponse.setApiErrors(appApiErrors);
            appApiResponse.setRequestSuccessful(true);

        }
        catch (Exception ex){
            log.error(ex.getMessage());
            AppApiError appApiError = new AppApiError("02", "Please try again, later");
            AppApiErrors appApiErrors = new AppApiErrors();
            List<AppApiError> listErrors = new ArrayList<>();
            listErrors.add(appApiError);
            appApiErrors.setApiErrorList(listErrors);
            appApiErrors.setErrorCount(1);
            appApiResponse.setApiErrors(appApiErrors);

        }

        return appApiResponse;

    }
}
