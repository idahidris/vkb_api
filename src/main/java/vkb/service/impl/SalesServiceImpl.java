//package vkb.service.impl;
//
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import vkb.controller.common.ApiResponseUtil;
//import vkb.controller.common.AppApiError;
//import vkb.controller.common.AppApiErrors;
//import vkb.controller.common.AppApiResponse;
//import vkb.dto.PageDto;
//import vkb.dto.SalesListRequestDto;
//import vkb.entity.Sales;
//import vkb.repository.SalesRepository;
//import vkb.service.SalesService;
//
//import java.util.ArrayList;
//import java.util.List;
//
//
//@Service
//@Slf4j
//public class SalesServiceImpl implements SalesService {
//
//    private final SalesRepository salesRepository;
//    private final ApiResponseUtil apiResponseUtil;
//
//    public SalesServiceImpl(SalesRepository salesRepository, ApiResponseUtil apiResponseUtil) {
//        this.salesRepository = salesRepository;
//        this.apiResponseUtil = apiResponseUtil;
//
//    }
//
//
//    @Override
//    public AppApiResponse register(SalesListRequestDto salesListRequestDto) {
//        AppApiResponse appApiResponse = new AppApiResponse();
//        List<Sales> sales = salesListRequestDto.toSales();
//        if(exists(sales)){//sales exists
//            AppApiError appApiError = new AppApiError("01", "sales already exists with this name and description");
//            AppApiErrors appApiErrors = new AppApiErrors();
//            List<AppApiError> listErrors = new ArrayList<>();
//            listErrors.add(appApiError);
//
//            appApiErrors.setApiErrorList(listErrors);
//            appApiErrors.setErrorCount(1);
//            appApiResponse.setApiErrors(appApiErrors);
//        }
//
//
//
//        else {//sales does not  exists
//
//
//
//            try{
//                appApiResponse.setResponseBody(salesRepository.saveAll(sales));
//                AppApiErrors appApiErrors = new AppApiErrors();
//                List<AppApiError> listErrors = new ArrayList<>();
//                appApiErrors.setApiErrorList(listErrors);
//                appApiErrors.setErrorCount(0);
//                appApiResponse.setApiErrors(appApiErrors);
//                appApiResponse.setRequestSuccessful(true);
//
//            }
//            catch (Exception ex){
//                log.error(ex.getMessage());
//                AppApiError appApiError = new AppApiError("02", "Please try again, later");
//                AppApiErrors appApiErrors = new AppApiErrors();
//                List<AppApiError> listErrors = new ArrayList<>();
//                listErrors.add(appApiError);
//                appApiErrors.setApiErrorList(listErrors);
//                appApiErrors.setErrorCount(1);
//                appApiResponse.setApiErrors(appApiErrors);
//
//            }
//
//
//
//        }
//        return appApiResponse;
//    }
//
//    @Override
//    @Transactional(readOnly = true)
//    public AppApiResponse fetchAll(PageDto pageDto) {
//        Pageable pageable = PageRequest.of(Integer.parseInt(pageDto.getPageNumber()), Integer.parseInt(pageDto.getPageSize()));
//        String searchValue = pageDto.getSearchValue();
//        if(searchValue==null || searchValue.trim().isEmpty())
//            return apiResponseUtil.entityPagedList(salesRepository.findAll(pageable), pageable);
//        else
//            return apiResponseUtil.entityPagedList(salesRepository.findAllBySalesIdLike("%"+pageDto.getSearchValue()+"%", pageable), pageable);
//    }
//
//    public boolean exists(List<Sales> sales){
//        return false;
//    }
//}
