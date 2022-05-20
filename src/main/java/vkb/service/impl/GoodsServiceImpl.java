package vkb.service.impl;


import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vkb.controller.common.ApiResponseUtil;
import vkb.controller.common.AppApiError;
import vkb.controller.common.AppApiErrors;
import vkb.controller.common.AppApiResponse;
import vkb.dto.GoodsRequestDto;
import vkb.dto.PageDto;
import vkb.entity.Goods;
import vkb.repository.GoodsRepository;
import vkb.service.GoodsService;

import java.util.ArrayList;
import java.util.List;


@Service
@Slf4j
public class GoodsServiceImpl implements GoodsService {

    private final GoodsRepository goodsRepository;
    private final ApiResponseUtil apiResponseUtil;

    public GoodsServiceImpl(GoodsRepository goodsRepository, ApiResponseUtil apiResponseUtil) {
        this.goodsRepository = goodsRepository;
        this.apiResponseUtil = apiResponseUtil;

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

    @Override
    public AppApiResponse findById(String id) {
        AppApiResponse appApiResponse = new AppApiResponse();
        try {
            List<Goods> goods = goodsRepository.findAllById(id);

            if(goods !=null && !goods.isEmpty()) {
                appApiResponse.setResponseBody(goods.get(0));
                AppApiErrors appApiErrors = new AppApiErrors();
                List<AppApiError> listErrors = new ArrayList<>();
                appApiErrors.setApiErrorList(listErrors);
                appApiErrors.setErrorCount(0);
                appApiResponse.setApiErrors(appApiErrors);
                appApiResponse.setRequestSuccessful(true);
                return appApiResponse;
            }
            else{

                AppApiError appApiError = new AppApiError("01", "invalid goods provided - "+id);
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

    @Override
    @Transactional(readOnly = true)
    public AppApiResponse fetchAll(PageDto pageDto) {
        Pageable pageable = PageRequest.of(Integer.parseInt(pageDto.getPageNumber()), Integer.parseInt(pageDto.getPageSize()));
        String searchValue = pageDto.getSearchValue();
        if(searchValue==null || searchValue.trim().isEmpty())
            return apiResponseUtil.entityPagedList(goodsRepository.findAll(pageable), pageable);
        else
            return apiResponseUtil.entityPagedList(goodsRepository.findAllByIdLike("%"+pageDto.getSearchValue()+"%", pageable), pageable);
    }

    @Override
    public AppApiResponse findByIdLike(String id) {
        Pageable pageable = PageRequest.of(0, 1);
        return apiResponseUtil.entityPagedList(goodsRepository.findAllByIdLike(id, pageable), pageable);

    }

    @Override
    public AppApiResponse update(Goods goods) {
        AppApiResponse appApiResponse = new AppApiResponse();
        Goods goodsExisting  = goodsRepository.findById(goods.getId()).orElse(null);
        if(goodsExisting==null){

            AppApiError appApiError = new AppApiError("01", "invalid  goods id provided");
            AppApiErrors appApiErrors = new AppApiErrors();
            List<AppApiError> listErrors = new ArrayList<>();
            listErrors.add(appApiError);

            appApiErrors.setApiErrorList(listErrors);
            appApiErrors.setErrorCount(1);
            appApiResponse.setApiErrors(appApiErrors);

            return  appApiResponse;

        }

        try{
            appApiResponse.setResponseBody(goodsRepository.save(goods));
            AppApiErrors appApiErrors = new AppApiErrors();
            List<AppApiError> listErrors = new ArrayList<>();
            appApiErrors.setApiErrorList(listErrors);
            appApiErrors.setErrorCount(0);
            appApiResponse.setApiErrors(appApiErrors);
            appApiResponse.setRequestSuccessful(true);
            return appApiResponse;


        }
        catch (Exception ex){
            log.error("Failed updating goods: {}",ex.getMessage());

            AppApiError appApiError = new AppApiError("02", "please try again later");
            AppApiErrors appApiErrors = new AppApiErrors();
            List<AppApiError> listErrors = new ArrayList<>();
            listErrors.add(appApiError);

            appApiErrors.setApiErrorList(listErrors);
            appApiErrors.setErrorCount(1);
            appApiResponse.setApiErrors(appApiErrors);

            return  appApiResponse;



        }

    }


    public boolean exists(Goods goods){
        List<Goods> goodsList = goodsRepository.findAllByNameAndDescription(goods.getName(), goods.getDescription());
        return !goodsList.isEmpty();
    }
}
