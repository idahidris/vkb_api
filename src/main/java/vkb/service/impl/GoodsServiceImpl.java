package vkb.service.impl;



import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vkb.controller.common.AppApiError;
import vkb.controller.common.AppApiErrors;
import vkb.controller.common.AppApiResponse;
import vkb.dto.GoodsRequestDto;
import vkb.entity.Goods;
import vkb.repository.GoodsRepository;
import vkb.service.GoodsService;

import java.util.ArrayList;
import java.util.List;


@Service
@Slf4j
public class GoodsServiceImpl implements GoodsService {

    private final GoodsRepository goodsRepository;

    public GoodsServiceImpl(GoodsRepository goodsRepository) {
        this.goodsRepository = goodsRepository;

    }


    @Override
    public AppApiResponse register(GoodsRequestDto goodsRequestDto) {
        AppApiResponse appApiResponse = new AppApiResponse();
        Goods goods = goodsRequestDto.toGoods();
        if(exists(goods)){//goods exists
            AppApiError appApiError = new AppApiError("01", "goods already exists with this name and description");
            AppApiErrors appApiErrors = new AppApiErrors();
            List<AppApiError> listErrors = new ArrayList<>();
            listErrors.add(appApiError);

            appApiErrors.setApiErrorList(listErrors);
            appApiErrors.setErrorCount(1);
            appApiResponse.setApiErrors(appApiErrors);
        }



        else {//goods does not  exists



            try{
                appApiResponse.setResponseBody(goodsRepository.save(goods));
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



        }
        return appApiResponse;
    }

    public boolean exists(Goods goods){
        List<Goods> goodsList = goodsRepository.findAllByNameAndDescription(goods.getName(), goods.getDescription());
        return !goodsList.isEmpty();
    }
}
