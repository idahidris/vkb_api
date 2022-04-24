package vkb.service;


import org.springframework.transaction.annotation.Transactional;
import vkb.controller.common.AppApiResponse;
import vkb.dto.PageDto;


public interface ReportService {

    @Transactional(readOnly = true)
    AppApiResponse reports();

}
