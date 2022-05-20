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
import vkb.entity.UserAccount;
import vkb.repository.UserAccountRepository;
import vkb.service.UserAccountService;
import vkb.util.CommonUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
@Slf4j
public class UserAccountServiceImpl implements UserAccountService {

    private final UserAccountRepository userAccountRepository;
    private final ApiResponseUtil apiResponseUtil;

    public UserAccountServiceImpl(UserAccountRepository userAccountRepository, ApiResponseUtil apiResponseUtil) {
        this.userAccountRepository = userAccountRepository;
        this.apiResponseUtil = apiResponseUtil;

    }


    @Override
    public AppApiResponse update(UserAccount userAccount) {
        AppApiResponse appApiResponse = new AppApiResponse();
        UserAccount userAccountExisting  = userAccountRepository.findById(userAccount.getId()).orElse(null);
        if(userAccountExisting==null){

            AppApiError appApiError = new AppApiError("01", "invalid  user id provided");
            AppApiErrors appApiErrors = new AppApiErrors();
            List<AppApiError> listErrors = new ArrayList<>();
            listErrors.add(appApiError);

            appApiErrors.setApiErrorList(listErrors);
            appApiErrors.setErrorCount(1);
            appApiResponse.setApiErrors(appApiErrors);

            return  appApiResponse;

        }

        userAccount.setRegisteredDate(userAccountExisting.getRegisteredDate());

        try{


            appApiResponse.setResponseBody(userAccountRepository.save(userAccount));
            AppApiErrors appApiErrors = new AppApiErrors();
            List<AppApiError> listErrors = new ArrayList<>();
            appApiErrors.setApiErrorList(listErrors);
            appApiErrors.setErrorCount(0);
            appApiResponse.setApiErrors(appApiErrors);
            appApiResponse.setRequestSuccessful(true);
            return appApiResponse;


        }
        catch (Exception ex){
            log.error("Failed updating user-account: {}",ex.getMessage());

            AppApiError appApiError = new AppApiError("02", "please try again later");
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
    public AppApiResponse register(UserAccount userAccount) {
        AppApiResponse appApiResponse = new AppApiResponse();

        if(exists(userAccount)){//goods exists
            String message="";
            if(userAccount.getEmail()!=null && !userAccount.getEmail().trim().isEmpty())
                 message = "user with email:"+userAccount.getEmail()+" already exists";
            else
                message = "user with phone number :"+userAccount.getPhone()+" already exists";
            AppApiError appApiError = new AppApiError("01", message);
            AppApiErrors appApiErrors = new AppApiErrors();
            List<AppApiError> listErrors = new ArrayList<>();
            listErrors.add(appApiError);

            appApiErrors.setApiErrorList(listErrors);
            appApiErrors.setErrorCount(1);
            appApiResponse.setApiErrors(appApiErrors);
        }



        else {//goods does not  exists



            try{
                userAccount.setId(CommonUtil.getID()+"");
                userAccount.setRegisteredDate(new Date());
                userAccount.setSubscription(userAccount.getId());
                appApiResponse.setResponseBody(userAccountRepository.save(userAccount));
                AppApiErrors appApiErrors = new AppApiErrors();
                List<AppApiError> listErrors = new ArrayList<>();
                appApiErrors.setApiErrorList(listErrors);
                appApiErrors.setErrorCount(0);
                appApiResponse.setApiErrors(appApiErrors);
                appApiResponse.setRequestSuccessful(true);

            }
            catch (Exception ex){
                log.error(ex.getMessage());
                AppApiError appApiError = new AppApiError("02", "Please try again, later");
                AppApiErrors appApiErrors = new AppApiErrors();
                List<AppApiError> listErrors = new ArrayList<>();
                listErrors.add(appApiError);
                appApiErrors.setApiErrorList(listErrors);
                appApiErrors.setErrorCount(1);
                appApiResponse.setApiErrors(appApiErrors);

            }



        }
        return appApiResponse;
    }

    @Override
    @Transactional(readOnly = true)
    public AppApiResponse fetchAll(PageDto pageDto) {
        Pageable pageable = PageRequest.of(Integer.parseInt(pageDto.getPageNumber()), Integer.parseInt(pageDto.getPageSize()));
        String searchValue = pageDto.getSearchValue();
        if(searchValue==null || searchValue.trim().isEmpty())
            return apiResponseUtil.entityPagedList(userAccountRepository.findAll(pageable), pageable);
        else
            return apiResponseUtil.entityPagedList(userAccountRepository.findAllByFirstNameLike("%"+pageDto.getSearchValue()+"%", pageable), pageable);
    }

    @Override
    public AppApiResponse findByFirstName(String firstName) {
        Pageable pageable = PageRequest.of(0, 1);
        return apiResponseUtil.entityPagedList(userAccountRepository.findAllByFirstNameLike(firstName, pageable), pageable);

    }

    @Override
    public AppApiResponse findById(String id) {
        AppApiResponse appApiResponse = new AppApiResponse();
        try {
            UserAccount userAccount = userAccountRepository.findById(id).orElse(null);

            if(userAccount !=null) {
                appApiResponse.setResponseBody(userAccount);
                AppApiErrors appApiErrors = new AppApiErrors();
                List<AppApiError> listErrors = new ArrayList<>();
                appApiErrors.setApiErrorList(listErrors);
                appApiErrors.setErrorCount(0);
                appApiResponse.setApiErrors(appApiErrors);
                appApiResponse.setRequestSuccessful(true);
                return appApiResponse;
            }
            else{

                AppApiError appApiError = new AppApiError("01", "invalid user id provided");
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






    public boolean exists(UserAccount userAccount){
        List<UserAccount> userAccounts ;
        if(userAccount.getEmail()!=null && !userAccount.getEmail().trim().isEmpty()) {
            userAccounts = userAccountRepository.findAllByEmail(userAccount.getEmail());
            return !userAccounts.isEmpty();
        }
        else if(userAccount.getPhone()!=null && !userAccount.getPhone().trim().isEmpty()) {
            userAccounts = userAccountRepository.findAllByPhone(userAccount.getPhone());
            return !userAccounts.isEmpty();
        }

        else return false;


    }

}
