package vkb.service;


import org.springframework.transaction.annotation.Transactional;
import vkb.controller.common.AppApiResponse;
import vkb.dto.PageDto;
import vkb.entity.UserAccount;


public interface UserAccountService {
    AppApiResponse register(UserAccount userAccount);
    AppApiResponse update(UserAccount userAccount);

    @Transactional(readOnly = true)
    AppApiResponse fetchAll(PageDto pageDto);
    AppApiResponse findByFirstName(String firstName);

    AppApiResponse findById(String firstName);
}
