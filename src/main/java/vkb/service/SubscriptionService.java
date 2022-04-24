package vkb.service;


import org.springframework.transaction.annotation.Transactional;
import vkb.controller.common.AppApiResponse;
import vkb.dto.PageDto;
import vkb.entity.Subscription;


public interface SubscriptionService {
    AppApiResponse register(Subscription subscription);
    AppApiResponse update(Subscription subscription);

    @Transactional(readOnly = true)
    AppApiResponse fetchAll(PageDto pageDto);
    AppApiResponse findById(String id);
}
