package vkb.service.impl;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vkb.controller.common.ApiResponseUtil;
import vkb.controller.common.AppApiError;
import vkb.controller.common.AppApiErrors;
import vkb.controller.common.AppApiResponse;
import vkb.dto.PageDto;
import vkb.entity.Goods;
import vkb.entity.ShippingOrder;
import vkb.entity.ShippingOrderItems;
import vkb.entity.ShoppingCart;
import vkb.repository.ShippingOrderItemsRepository;
import vkb.repository.ShippingOrderRepository;
import vkb.repository.ShoppingCartRepository;
import vkb.service.ShippingOrderService;
import vkb.util.CommonUtil;

import java.util.*;


@Service
@Slf4j
public class ShippingOrderServiceImpl implements ShippingOrderService {

    private final ShippingOrderRepository shippingOrderRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final ShippingOrderItemsRepository shippingOrderItemsRepository;
    private final ApiResponseUtil apiResponseUtil;



    public ShippingOrderServiceImpl(ShippingOrderItemsRepository shippingOrderItemsRepository,ShoppingCartRepository shoppingCartRepository, ShippingOrderRepository shippingOrderRepository, ApiResponseUtil apiResponseUtil) {
        this.shippingOrderRepository = shippingOrderRepository;
        this.shoppingCartRepository = shoppingCartRepository;
        this.apiResponseUtil = apiResponseUtil;
        this.shippingOrderItemsRepository = shippingOrderItemsRepository;

    }

    @Override
    @Transactional(readOnly = true)
    public AppApiResponse fetchAll(PageDto pageDto) {
        Pageable pageable = PageRequest.of(Integer.parseInt(pageDto.getPageNumber()), Integer.parseInt(pageDto.getPageSize()));
        String searchValue = pageDto.getSearchValue();
        if(searchValue==null || searchValue.trim().isEmpty())
            return apiResponseUtil.entityPagedList(shippingOrderRepository.findAll(pageable), pageable);
        else
            return apiResponseUtil.entityPagedList(shippingOrderRepository.findAllByBatchIdLike("%"+pageDto.getSearchValue()+"%", pageable), pageable);
    }

    @Override
    public AppApiResponse register(ShippingOrder shippingOrder) {
        AppApiResponse appApiResponse = new AppApiResponse();
        try {
            List<ShoppingCart> orderItems = shoppingCartRepository.findAll();
            TypeReference<List<ShippingOrderItems>> typeRef = new TypeReference<List<ShippingOrderItems>>() {
            };



            shippingOrder.setBatchId((shippingOrder.getShippingVendor().charAt(0) + "").toUpperCase().trim() + CommonUtil.getID());
            shippingOrder.setOrderDate(new Date());
            shippingOrder.setStatus("Initiated");


            List<ShippingOrderItems> orderItemsList = (new ObjectMapper().convertValue(orderItems, typeRef));
            log.info("Object conversion from cart to order successful {}", orderItemsList.size());

            for(ShippingOrderItems items: orderItemsList){
                items.setShippingOrder(shippingOrder);
                items.setItemId("N"+CommonUtil.getID());
            }


            Map<String, Object> response = new HashMap<>();
            response.put("batchId", shippingOrder.getBatchId());

            log.info("shipping order saved successfully: " +shippingOrder.getBatchId());

            response.put("data", shippingOrderItemsRepository.saveAll(orderItemsList));
            log.info("shipping order items saved successfully: " +orderItemsList.size());
            shoppingCartRepository.deleteAll();
            log.info("shipping carts emptied successfully: " +orderItemsList.size());

            appApiResponse.setResponseBody(response);
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


    @Override
    public AppApiResponse findById(String id) {
        AppApiResponse appApiResponse = new AppApiResponse();
        try {
            ShippingOrder  order = shippingOrderRepository.findById(id).orElse(null);

            if(order !=null ) {
                appApiResponse.setResponseBody(order);
                AppApiErrors appApiErrors = new AppApiErrors();
                List<AppApiError> listErrors = new ArrayList<>();
                appApiErrors.setApiErrorList(listErrors);
                appApiErrors.setErrorCount(0);
                appApiResponse.setApiErrors(appApiErrors);
                appApiResponse.setRequestSuccessful(true);
                return appApiResponse;
            }
            else{

                AppApiError appApiError = new AppApiError("01", "invalid order provided - "+id);
                AppApiErrors appApiErrors = new AppApiErrors();
                List<AppApiError> listErrors = new ArrayList<>();
                listErrors.add(appApiError);

                appApiErrors.setApiErrorList(listErrors);
                appApiErrors.setErrorCount(1);
                appApiResponse.setApiErrors(appApiErrors);

                return  appApiResponse;

            }
        }
        catch (Exception ex){

            AppApiError appApiError = new AppApiError("01", "please try again, later");
            AppApiErrors appApiErrors = new AppApiErrors();
            List<AppApiError> listErrors = new ArrayList<>();
            listErrors.add(appApiError);

            appApiErrors.setApiErrorList(listErrors);
            appApiErrors.setErrorCount(1);
            appApiResponse.setApiErrors(appApiErrors);

            return  appApiResponse;

        }

    }



}
