package vkb.service;


import org.springframework.transaction.annotation.Transactional;
import vkb.controller.common.AppApiResponse;
import vkb.dto.PageDto;


public interface SalesService {

    @Transactional(readOnly = true)
    AppApiResponse fetchAll(PageDto pageDto);

    @Transactional(readOnly = true)
    AppApiResponse reports();
}
