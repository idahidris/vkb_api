package vkb.service.impl;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vkb.controller.common.AppApiError;
import vkb.controller.common.AppApiErrors;
import vkb.controller.common.AppApiResponse;
import vkb.entity.Report;
import vkb.repository.GoodsRepository;
import vkb.repository.SalesRepository;
import vkb.repository.SubscriptionRepository;
import vkb.service.ReportService;
import vkb.util.DateFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.*;


@Service
@Slf4j
public class ReportServiceImpl implements ReportService {

    private final SalesRepository salesRepository;
    private final GoodsRepository goodsRepository;
    private final SubscriptionRepository subscriptionRepository;

    static final  String MONTHLY_INCOME = "monthlyIncome";
    static final  String GOODS_VALUE = "goodsValue";
    static final  String SERVICE_TYPE ="service_type";
    static final String STATUS_REPORT = "status_report";

    public ReportServiceImpl(SalesRepository salesRepository, SubscriptionRepository subscriptionRepository, GoodsRepository goodsRepository) {
        this.salesRepository = salesRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.goodsRepository = goodsRepository;

    }


    @Override
    public AppApiResponse reports()  {
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
        reports.put(MONTHLY_INCOME, monthlyIncome);


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
        reports.put(GOODS_VALUE, report);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date start = sdf.parse(startDate);
            Date end = sdf.parse(endDate);

            List<Report> subscriptionServiceType= subscriptionRepository.fetchServiceTypeReport(start, end);
            reports.put(SERVICE_TYPE, subscriptionServiceType);


            List<Report> statusReport = subscriptionRepository.fetchStatusReport( start, end);
            reports.put(STATUS_REPORT, statusReport);

        }
        catch (ParseException p){
            log.error(p.getMessage());
        }




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
