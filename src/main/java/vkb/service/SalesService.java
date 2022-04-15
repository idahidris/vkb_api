//package vkb.service;
//
//
//import org.springframework.transaction.annotation.Transactional;
//import vkb.controller.common.AppApiResponse;
//import vkb.dto.PageDto;
//import vkb.dto.SalesListRequestDto;
//
//
//public interface SalesService {
//    AppApiResponse register(SalesListRequestDto salesListRequestDto);
//
//    @Transactional(readOnly = true)
//    AppApiResponse fetchAll(PageDto pageDto);
//}
