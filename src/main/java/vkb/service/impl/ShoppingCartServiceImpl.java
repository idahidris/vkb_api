package vkb.service.impl;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vkb.controller.common.ApiResponseUtil;
import vkb.controller.common.AppApiError;
import vkb.controller.common.AppApiErrors;
import vkb.controller.common.AppApiResponse;
import vkb.entity.ShoppingCart;
import vkb.repository.ShoppingCartRepository;
import vkb.service.ShoppingCartService;
import vkb.util.CommonUtil;

import java.text.DecimalFormat;
import java.util.*;


@Service
@Slf4j
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private final ApiResponseUtil apiResponseUtil;
    private final ShoppingCartRepository shoppingCartRepository;

    public ShoppingCartServiceImpl( ApiResponseUtil apiResponseUtil, ShoppingCartRepository shoppingCartRepository) {
        this.apiResponseUtil = apiResponseUtil;
        this.shoppingCartRepository = shoppingCartRepository;

    }


    @Override
    public AppApiResponse add(ShoppingCart shoppingCart) {
        AppApiResponse appApiResponse = new AppApiResponse();

        try{
             Boolean exists = false;

             if(exists) {
                AppApiError appApiError = new AppApiError("01", "Shopping Cart exists already");
                AppApiErrors appApiErrors = new AppApiErrors();
                List<AppApiError> listErrors = new ArrayList<>();
                listErrors.add(appApiError);
                appApiErrors.setApiErrorList(listErrors);
                appApiErrors.setErrorCount(1);
                appApiResponse.setApiErrors(appApiErrors);
            }

            else {
                shoppingCart.setId((shoppingCart.getProductName().charAt(0)+"0").toUpperCase().trim()+CommonUtil.getID());
                shoppingCart.setCreatedDate(new Date());
                appApiResponse.setResponseBody(shoppingCartRepository.save(shoppingCart));
                AppApiErrors appApiErrors = new AppApiErrors();
                List<AppApiError> listErrors = new ArrayList<>();
                appApiErrors.setApiErrorList(listErrors);
                appApiErrors.setErrorCount(0);
                appApiResponse.setApiErrors(appApiErrors);
                appApiResponse.setRequestSuccessful(true);

            }

        }
        catch (Exception ex){
            ex.printStackTrace();
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
            ShoppingCart cart = shoppingCartRepository.findById(id).orElse(null);
            if(cart !=null) {
                    shoppingCartRepository.delete(cart);
                    return fetchAll();
                }
            else {
                    AppApiError appApiError = new AppApiError("02", "Nothing to remove, item not found");
                    AppApiErrors appApiErrors = new AppApiErrors();
                    List<AppApiError> listErrors = new ArrayList<>();
                    listErrors.add(appApiError);
                    appApiErrors.setApiErrorList(listErrors);
                    appApiErrors.setErrorCount(1);
                    appApiResponse.setApiErrors(appApiErrors);
                    appApiResponse.setResponseBody(fetchAll());

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
    public AppApiResponse fetchAll() {
        AppApiResponse appApiResponse = new AppApiResponse();

        try{
            List<ShoppingCart> carts = shoppingCartRepository.findAll();
            Map<String, Object> result = new HashMap<>();
            double total = 0;
            for(ShoppingCart cart : carts){
                    double qty = cart.getQuantity()==null?0:cart.getQuantity();
                    double price = cart.getUnitPrice()==null?0:cart.getUnitPrice();
                    total = total + (qty * price* cart.getRate());
            }

            result.put("carts", carts);
            result.put("total", new DecimalFormat("#,###.00").format(total));
            appApiResponse.setResponseBody(result);
            AppApiErrors appApiErrors = new AppApiErrors();
            List<AppApiError> listErrors = new ArrayList<>();
            appApiErrors.setApiErrorList(listErrors);
            appApiErrors.setErrorCount(0);
            appApiResponse.setApiErrors(appApiErrors);
            appApiResponse.setRequestSuccessful(true);

        }
        catch (Exception ex){
            ex.printStackTrace();
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
