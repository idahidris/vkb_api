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
import vkb.entity.Subscription;
import vkb.entity.UserAccount;
import vkb.repository.SubscriptionRepository;
import vkb.repository.UserAccountRepository;
import vkb.service.EmailService;
import vkb.service.SubscriptionService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;


@Service
@Slf4j
public class SubscriptionServiceImpl implements SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final ApiResponseUtil apiResponseUtil;
    private final UserAccountRepository userAccountRepository;
    private final EmailService emailService;

    public SubscriptionServiceImpl(SubscriptionRepository subscriptionRepository, ApiResponseUtil apiResponseUtil, UserAccountRepository userAccountRepository, EmailService emailService) {
        this.subscriptionRepository = subscriptionRepository;
        this.apiResponseUtil = apiResponseUtil;
        this.userAccountRepository = userAccountRepository;
        this.emailService = emailService;

    }


    @Override
    public AppApiResponse register(Subscription subscription) {
        AppApiResponse appApiResponse = new AppApiResponse();
        UserAccount account  = userAccountRepository.findById(subscription.getCustomerId()).orElse(null);
        if(account==null){

            AppApiError appApiError = new AppApiError("01", "invalid  customer id provided");
            AppApiErrors appApiErrors = new AppApiErrors();
            List<AppApiError> listErrors = new ArrayList<>();
            listErrors.add(appApiError);

            appApiErrors.setApiErrorList(listErrors);
            appApiErrors.setErrorCount(1);
            appApiResponse.setApiErrors(appApiErrors);

            return  appApiResponse;

        }

        subscription.setId(UUID.randomUUID().toString());
        subscription.setSubscriptionDate(new Date());

        try{

            if(existing(subscription)){


                AppApiError appApiError = new AppApiError("01", "Subscription already exists");
                AppApiErrors appApiErrors = new AppApiErrors();
                List<AppApiError> listErrors = new ArrayList<>();
                listErrors.add(appApiError);

                appApiErrors.setApiErrorList(listErrors);
                appApiErrors.setErrorCount(1);
                appApiResponse.setApiErrors(appApiErrors);

                return  appApiResponse;


            }

            appApiResponse.setResponseBody(subscriptionRepository.save(subscription));
            AppApiErrors appApiErrors = new AppApiErrors();
            List<AppApiError> listErrors = new ArrayList<>();
            appApiErrors.setApiErrorList(listErrors);
            appApiErrors.setErrorCount(0);
            appApiResponse.setApiErrors(appApiErrors);
            appApiResponse.setRequestSuccessful(true);
            if(account.getEmail() !=null) {
                emailService.sendMail(account, "initiated", subscription, "Welcome "+account.getFirstName());
                log.info("mail sent successfully");
            }
            else {
                log.info("User {} does not have  email for notification ", account.getId());
            }
            return appApiResponse;


        }
        catch (Exception ex){

            AppApiError appApiError = new AppApiError("01", "please try again later");
            AppApiErrors appApiErrors = new AppApiErrors();
            List<AppApiError> listErrors = new ArrayList<>();
            listErrors.add(appApiError);

            appApiErrors.setApiErrorList(listErrors);
            appApiErrors.setErrorCount(1);
            appApiResponse.setApiErrors(appApiErrors);

            return  appApiResponse;



        }



    }

    @Override
    public AppApiResponse update(Subscription subscription) {
        AppApiResponse appApiResponse = new AppApiResponse();
        Subscription subscriptionExisting  = subscriptionRepository.findById(subscription.getId()).orElse(null);
        if(subscriptionExisting==null){

            AppApiError appApiError = new AppApiError("01", "invalid  subscription id provided");
            AppApiErrors appApiErrors = new AppApiErrors();
            List<AppApiError> listErrors = new ArrayList<>();
            listErrors.add(appApiError);

            appApiErrors.setApiErrorList(listErrors);
            appApiErrors.setErrorCount(1);
            appApiResponse.setApiErrors(appApiErrors);

            return  appApiResponse;

        }

        boolean isStatusCompleted = !(subscriptionExisting.getStatus().equals(subscription.getStatus())) && subscription.getStatus().equals("Completed");

        subscription.setServiceType(subscriptionExisting.getServiceType());
        subscription.setSubscriptionDate(subscriptionExisting.getSubscriptionDate());
        subscription.setCustomerId(subscriptionExisting.getCustomerId());

        try{


            appApiResponse.setResponseBody(subscriptionRepository.save(subscription));
            AppApiErrors appApiErrors = new AppApiErrors();
            List<AppApiError> listErrors = new ArrayList<>();
            appApiErrors.setApiErrorList(listErrors);
            appApiErrors.setErrorCount(0);
            appApiResponse.setApiErrors(appApiErrors);
            appApiResponse.setRequestSuccessful(true);

            if(isStatusCompleted){
                log.info("about  to send completion update");
                UserAccount account=userAccountRepository.findById(subscription.getCustomerId()).orElse(null);


                if(account!=null && account.getEmail() !=null) {
                    emailService.sendMail(account, "completed", subscription, "Congratulations "+account.getFirstName());
                    log.info("mail sent successfully");
                }
                else {
                    log.info("subscription {} does not have  email for notification ", subscription.getId());
                }
            }
            return appApiResponse;


        }
        catch (Exception ex){
            log.error("Failed updating subscription: {}",ex.getMessage());

            AppApiError appApiError = new AppApiError("01", "please try again later");
            AppApiErrors appApiErrors = new AppApiErrors();
            List<AppApiError> listErrors = new ArrayList<>();
            listErrors.add(appApiError);

            appApiErrors.setApiErrorList(listErrors);
            appApiErrors.setErrorCount(1);
            appApiResponse.setApiErrors(appApiErrors);

            return  appApiResponse;



        }

    }

    @Override
    @Transactional(readOnly = true)
    public AppApiResponse fetchAll(PageDto pageDto) {
        Pageable pageable = PageRequest.of(Integer.parseInt(pageDto.getPageNumber()), Integer.parseInt(pageDto.getPageSize()));
        String searchValue = pageDto.getSearchValue();
        if(searchValue==null || searchValue.trim().isEmpty())
            return apiResponseUtil.entityPagedList(subscriptionRepository.findAll(pageable), pageable);
        else
            return apiResponseUtil.entityPagedList(subscriptionRepository.findAllByCustomerIdLike("%"+pageDto.getSearchValue()+"%", pageable), pageable);
    }

    @Override
    public AppApiResponse findById(String id) {
        AppApiResponse appApiResponse = new AppApiResponse();
        try {
            Subscription subscription = subscriptionRepository.findById(id).orElse(null);

            if(subscription !=null) {
                appApiResponse.setResponseBody(subscription);
                AppApiErrors appApiErrors = new AppApiErrors();
                List<AppApiError> listErrors = new ArrayList<>();
                appApiErrors.setApiErrorList(listErrors);
                appApiErrors.setErrorCount(0);
                appApiResponse.setApiErrors(appApiErrors);
                appApiResponse.setRequestSuccessful(true);
                return appApiResponse;
            }
            else{

                AppApiError appApiError = new AppApiError("01", "invalid subscription provided");
                AppApiErrors appApiErrors = new AppApiErrors();
                List<AppApiError> listErrors = new ArrayList<>();
                listErrors.add(appApiError);

                appApiErrors.setApiErrorList(listErrors);
                appApiErrors.setErrorCount(1);
                appApiResponse.setApiErrors(appApiErrors);

                return  appApiResponse;

            }
        }
        catch (Exception ex){

            AppApiError appApiError = new AppApiError("01", "please try again, later");
            AppApiErrors appApiErrors = new AppApiErrors();
            List<AppApiError> listErrors = new ArrayList<>();
            listErrors.add(appApiError);

            appApiErrors.setApiErrorList(listErrors);
            appApiErrors.setErrorCount(1);
            appApiResponse.setApiErrors(appApiErrors);

            return  appApiResponse;

        }

    }

    public boolean existing(Subscription subscription){
        return !subscriptionRepository.findAllByDescriptionAndServiceTitleAndServiceTypeAndPrice(subscription.getDescription(), subscription.getServiceTitle(),subscription.getServiceType(), subscription.getPrice() ).isEmpty();

    }


}
