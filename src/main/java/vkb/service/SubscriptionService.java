package vkb.service;


import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import vkb.controller.common.AppApiResponse;
import vkb.dto.PageDto;
import vkb.entity.Subscription;


public interface SubscriptionService {
    AppApiResponse register(Subscription subscription, MultipartFile file, String path);
    AppApiResponse update(Subscription subscription, MultipartFile file, String path);

    @Transactional(readOnly = true)
    AppApiResponse fetchAll(PageDto pageDto);
    AppApiResponse findById(String id);
}
