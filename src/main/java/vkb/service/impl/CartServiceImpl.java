package vkb.service.impl;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vkb.controller.common.ApiResponseUtil;
import vkb.controller.common.AppApiError;
import vkb.controller.common.AppApiErrors;
import vkb.controller.common.AppApiResponse;
import vkb.entity.Cart;
import vkb.entity.Goods;
import vkb.entity.Sales;
import vkb.repository.CartRepository;
import vkb.repository.GoodsRepository;
import vkb.repository.SalesRepository;
import vkb.service.CartService;
import vkb.util.CommonUtil;

import java.text.DecimalFormat;
import java.util.*;


@Service
@Slf4j
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final GoodsRepository goodsRepository;
    private final ApiResponseUtil apiResponseUtil;
    private final SalesRepository salesRepository;
    private String defaultUserId="admin";
    private String storeName = "Marshy Hill";

    public CartServiceImpl(CartRepository cartRepository, GoodsRepository goodsRepository, ApiResponseUtil apiResponseUtil, SalesRepository salesRepository) {
        this.cartRepository = cartRepository;
        this.goodsRepository = goodsRepository;
        this.apiResponseUtil = apiResponseUtil;
        this.salesRepository = salesRepository;

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
                    cart.setUserId(defaultUserId);
                    cart.setItemId(goods.getId());
                    cart.setQuantity(qty + "");
                    cart.setStoreName("AEKINS");
                    cart.setCreatedDate(new Date().toLocaleString());
                }
                else{
                     prevQty = Integer.parseInt(cart.getQuantity());
                    cart.setQuantity(qty+"");
                }


                int balance = qty - prevQty;

                int newGoodsBalance = goods.getQuantity()  - balance;

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

                goods.setQuantity(newGoodsBalance);
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
    public AppApiResponse remove(String id) {
        AppApiResponse appApiResponse = new AppApiResponse();

        try{
            Cart cart = cartRepository.findById(id).orElse(null);
            if(cart !=null) {
                Goods goods = goodsRepository.findById(id).orElse(null);
                if(goods !=null) {
                    int newQuantity =goods.getQuantity() + Integer.parseInt(cart.getQuantity());
                    goods.setQuantity(newQuantity);
                    cartRepository.delete(cart);
                    goodsRepository.save(goods);
                    return fetchAllByUserId("admin");
                }
                else{
                    AppApiError appApiError = new AppApiError("02", "item not found");
                    AppApiErrors appApiErrors = new AppApiErrors();
                    List<AppApiError> listErrors = new ArrayList<>();
                    listErrors.add(appApiError);
                    appApiErrors.setApiErrorList(listErrors);
                    appApiErrors.setErrorCount(1);
                    appApiResponse.setApiErrors(appApiErrors);

                }
            }
            else{
                AppApiError appApiError = new AppApiError("02", "item not found");
                AppApiErrors appApiErrors = new AppApiErrors();
                List<AppApiError> listErrors = new ArrayList<>();
                listErrors.add(appApiError);
                appApiErrors.setApiErrorList(listErrors);
                appApiErrors.setErrorCount(1);
                appApiResponse.setApiErrors(appApiErrors);

            }

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
            List<Cart> carts = cartRepository.findAllByUserId(defaultUserId);
            List<Map<String, Object>> result = new ArrayList();
            double total = 0;
            for(Cart cart : carts){

                Goods goods = goodsRepository.findById(cart.getItemId()).orElse(null);
                if(goods !=null) {
                    Map<String, Object> obj = new HashMap<>();
                    obj.put("qty", cart.getQuantity());
                    obj.put("goods", goods);
                    result.add(obj);
                    total = total + (Integer.parseInt(cart.getQuantity()) * goods.getUnitPrice());
                }
            }
            Map<String, Object> resp = new HashMap<>();
            resp.put("carts", result);
            resp.put("total", new DecimalFormat("#,###.00").format(total));

            appApiResponse.setResponseBody(resp);
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
    public AppApiResponse cartToSales(String userId, String customerRef) {

        AppApiResponse appApiResponse = new AppApiResponse();


        if(customerRef==null || customerRef.isEmpty()|| customerRef.equals("default"))
            customerRef = "C"+CommonUtil.getID();

        List<Cart> carts = cartRepository.findAllByUserId(defaultUserId);
        List<Sales> salesList = new ArrayList<>();
        String batchId ="B"+CommonUtil.getID();
        for (Cart cart: carts){

            Goods goods = goodsRepository.findById(cart.getItemId()).orElse(null);
            if(goods !=null) {
                Sales sales = new Sales();
                sales.setBatchId(batchId);
                sales.setSalesId(storeName.charAt(0)+"0"+CommonUtil.getID());
                sales.setTotalPrice((Integer.parseInt(cart.getQuantity()) * goods.getUnitPrice()) + "");
                sales.setUnitPrice(goods.getUnitPrice()+"");
                sales.setManufacturedDate(goods.getManufacturedDate());
                sales.setSalesDate(new Date());
                sales.setItemName(goods.getName());
                sales.setItemDescription(goods.getDescription());
                sales.setExpiryDate(goods.getExpiryDate());
                sales.setCustomerRef(customerRef);
                sales.setItemId(goods.getId());
                sales.setStoreName(storeName);
                sales.setQuantity(cart.getQuantity());
                salesList.add(sales);
            }
        }

        if(salesList.isEmpty()){
            AppApiError appApiError = new AppApiError("02", "no  item found in cart");
            AppApiErrors appApiErrors = new AppApiErrors();
            List<AppApiError> listErrors = new ArrayList<>();
            listErrors.add(appApiError);
            appApiErrors.setApiErrorList(listErrors);
            appApiErrors.setErrorCount(1);
            appApiResponse.setApiErrors(appApiErrors);
            return appApiResponse;

        }

        Map<String, Object> response = new HashMap<>();
        response.put("batchId", batchId);
        response.put("data",salesRepository.saveAll(salesList) );

        appApiResponse.setResponseBody(response);
        AppApiErrors appApiErrors = new AppApiErrors();
        List<AppApiError> listErrors = new ArrayList<>();
        appApiErrors.setApiErrorList(listErrors);
        appApiErrors.setErrorCount(0);
        appApiResponse.setApiErrors(appApiErrors);
        appApiResponse.setRequestSuccessful(true);

        cartRepository.deleteAllByUserId(defaultUserId);


        return appApiResponse;


    }
}
