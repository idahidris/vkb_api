package vkb.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vkb.controller.common.AppApiResponse;
import vkb.service.ReportService;


@RestController
@RequestMapping(ApiController.BASE_PATH)
@Slf4j
@CrossOrigin
public class ReportsController extends ApiController {

    private final ReportService reportService;
    ObjectMapper objectMapper = new ObjectMapper();



    public ReportsController(ReportService reportService) {

        this.reportService = reportService;
    }




    @GetMapping(value = REPORT+"/dashboard")
    public AppApiResponse getSalesReport() throws JsonProcessingException {


        log.info("************************** start dashboard report api **************************");
        AppApiResponse appApiResponse = reportService.reports();
        log.info("RESPONSE==>"+objectMapper.writeValueAsString(appApiResponse));
        log.info("********************* finished executing get all sales report api *************************");

        return appApiResponse;
    }






}
