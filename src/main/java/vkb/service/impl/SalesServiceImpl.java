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
import vkb.dto.PageDto;
import vkb.entity.Report;
import vkb.repository.GoodsRepository;
import vkb.repository.SalesRepository;
import vkb.service.SalesService;

import java.time.ZoneId;
import java.util.*;


@Service
@Slf4j
public class SalesServiceImpl implements SalesService {

    private final SalesRepository salesRepository;
    private final ApiResponseUtil apiResponseUtil;
    private final GoodsRepository goodsRepository;

    public SalesServiceImpl(SalesRepository salesRepository, ApiResponseUtil apiResponseUtil, GoodsRepository goodsRepository) {
        this.salesRepository = salesRepository;
        this.apiResponseUtil = apiResponseUtil;
        this.goodsRepository = goodsRepository;

    }


    @Override
    @Transactional(readOnly = true)
    public AppApiResponse fetchAll(PageDto pageDto) {
        Pageable pageable = PageRequest.of(Integer.parseInt(pageDto.getPageNumber()), Integer.parseInt(pageDto.getPageSize()));
        String searchValue = pageDto.getSearchValue();
        if(searchValue==null || searchValue.trim().isEmpty())
            return apiResponseUtil.entityPagedList(salesRepository.findAll(pageable), pageable);
        else
            return apiResponseUtil.entityPagedList(salesRepository.findAllByBatchIdLike("%"+pageDto.getSearchValue()+"%", pageable), pageable);
    }

    @Override
    public AppApiResponse reports() {
        AppApiResponse appApiResponse = new AppApiResponse();
        Map<String, Object> reports = new HashMap<>();

        long thisYear = Calendar.getInstance().get(Calendar.YEAR);
        long thisMonth = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getMonthValue();
        long thisDate = Calendar.getInstance().get(Calendar.DATE);

        String endDate = thisYear + "-"+ String.format("%02d",thisMonth) + "-"+ String.format("%02d", (thisDate+1));

        String startDate = thisYear + "-01-01";

        log.info("start Date==>"+ startDate +"  , end Date ==>"+ endDate);

        List<Report> monthlyIncome = salesRepository.fetchMonthEarnings(startDate, endDate);
        log.info("fetch ===>"+ monthlyIncome.size());


        reports.put("monthlyIncome", monthlyIncome);


        List<Report> reportList = goodsRepository.fetchGoodsValue();
        log.info("report is null==>"+ reportList.isEmpty());

        Report report =reportList.isEmpty()? new Report() {
            @Override
            public String getFactor() {
                return "";
            }

            @Override
            public double getMeasure() {
                return 0;
            }
        } : reportList.get(0);
        reports.put("goodsValue", report);





        appApiResponse.setResponseBody(reports);
        AppApiErrors appApiErrors = new AppApiErrors();
        List<AppApiError> listErrors = new ArrayList<>();
        appApiErrors.setApiErrorList(listErrors);
        appApiErrors.setErrorCount(0);
        appApiResponse.setApiErrors(appApiErrors);
        appApiResponse.setRequestSuccessful(true);


        return appApiResponse;

    }



}
