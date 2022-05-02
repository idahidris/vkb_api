package vkb.service.impl;


import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
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

import java.text.SimpleDateFormat;
import java.util.*;


@Service
@Slf4j
public class SubscriptionServiceImpl implements SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final ApiResponseUtil apiResponseUtil;
    private final UserAccountRepository userAccountRepository;
    private final EmailService emailService;
    private final FileStorageService fileStorageService;

    ObjectMapper objectMapper = new ObjectMapper().
            enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS).configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES,true).
            enable(SerializationFeature.INDENT_OUTPUT).
            setDateFormat(new SimpleDateFormat("dd-MM-yyyy")).
            configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).
            configure(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE, false);

    public SubscriptionServiceImpl(SubscriptionRepository subscriptionRepository, ApiResponseUtil apiResponseUtil, UserAccountRepository userAccountRepository, EmailService emailService, FileStorageService fileStorageService) {
        this.subscriptionRepository = subscriptionRepository;
        this.apiResponseUtil = apiResponseUtil;
        this.userAccountRepository = userAccountRepository;
        this.emailService = emailService;
        this.fileStorageService = fileStorageService;

    }


    @Override
    public AppApiResponse register(Subscription subscription, MultipartFile file, String path) {
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



            if(file !=null) {
                String newName =null;
                log.info(file.getContentType()+"file found!!! saving file now:::" +file.getOriginalFilename());
                String defaultExt=".docx";
                log.info("default file extension is {}", defaultExt );
                String documentLink = subscription.getDocumentLink();

                if(documentLink !=null){
                    log.info("File name found !!! {}", documentLink);
                    int lastIndexExt = documentLink.lastIndexOf('.');
                    if(lastIndexExt>0)
                        defaultExt=documentLink.substring(lastIndexExt);

                    newName=file.getOriginalFilename()+defaultExt;
                    log.info("New File name is {}", newName);
                }
                String fileName = fileStorageService.storeFile(file, newName);
                log.info("file was saved with  the name {}", fileName);

                String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                        .path(path)
                        .path(newName==null?fileName:newName)
                        .toUriString();

                log.info("The file path for download is {}"+ fileDownloadUri);


                Map<String, String> linkDetails = new HashMap<>();
                linkDetails.put("fileName", documentLink);
                linkDetails.put("path", fileDownloadUri);
                String newDocumentLink = objectMapper.writeValueAsString(linkDetails);
                log.info("The document info as saved in database is {}", newDocumentLink);
                subscription.setDocumentLink(newDocumentLink);
                log.info("saving file now:::" + fileName+ "::: ended");
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
    public AppApiResponse update(Subscription subscription, MultipartFile file, String path){
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


            if(file !=null) {
                String newName =null;
                log.info(file.getContentType()+"file found!!! saving file now:::" +file.getOriginalFilename());
                String defaultExt=".docx";
                log.info("default file extension is {}", defaultExt );
                String documentLink = subscriptionExisting.getDocumentLink();
                Map<String, String> prevLink = getPreviousLink(documentLink);
                String reqDocumentLink = subscription.getDocumentLink();

                if(documentLink !=null) {

                    String paths = prevLink.get("path");
                    if (paths == null) {
                        log.info("previous link is invalid or does not exists:::" + documentLink);
                    } else {

                        int lastIndexPath = paths.lastIndexOf("/");
                        newName = paths.substring(lastIndexPath + 1);
                        log.info("Previous File path name is {}", newName);
                    }

                }

                if(reqDocumentLink !=null && newName==null){
                    log.info("File name found in request !!! {}", reqDocumentLink);
                    int lastIndexExt = reqDocumentLink.lastIndexOf('.');
                    if(lastIndexExt>0)
                        defaultExt=reqDocumentLink.substring(lastIndexExt);
                    newName=file.getOriginalFilename()+defaultExt;
                    log.info("New File name is {}", newName);
                }

                String fileName = fileStorageService.storeFile(file, newName);
                log.info("file was saved with  the name {}", fileName);

                String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                        .path(path)
                        .path(newName==null?fileName:newName)
                        .toUriString();

                log.info("The file path for download is {}"+ fileDownloadUri);


                 if(reqDocumentLink !=null){
                    Map<String, String> linkDetails = new HashMap<>();
                    linkDetails.put("fileName", reqDocumentLink);
                    linkDetails.put("path", fileDownloadUri);
                    String newDocumentLink = objectMapper.writeValueAsString(linkDetails);
                    log.info("The document info as saved in database is {}", newDocumentLink);
                    subscription.setDocumentLink(newDocumentLink);
                    log.info("saving file now:::" + fileName + "::: ended");
                }
                else if(prevLink.isEmpty()) {
                    Map<String, String> linkDetails = new HashMap<>();
                    linkDetails.put("fileName", fileName);
                    linkDetails.put("path", fileDownloadUri);
                    String newDocumentLink = objectMapper.writeValueAsString(linkDetails);
                    log.info("The document info as saved in database is {}", newDocumentLink);
                    subscription.setDocumentLink(newDocumentLink);
                    log.info("saving file now:::" + fileName + "::: ended");
                }

                else {
                    String newDocumentLink = objectMapper.writeValueAsString(prevLink);
                    log.info("The document info as saved in database is {}", newDocumentLink);
                    subscription.setDocumentLink(newDocumentLink);
                    log.info("saving file now:::" + fileName + "::: ended");

                }


            }

            else {
                subscription.setDocumentLink(subscriptionExisting.getDocumentLink());
            }


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

    public Map<String, String> getPreviousLink(String link){
        Map<String, String> result = new HashMap<>();

        try{
             TypeReference<HashMap<String, String>> ref= new TypeReference<HashMap<String, String>>(){};
             result = objectMapper.readValue(link, ref);

        }
        catch (Exception ex){
            return result;
        }

        return result;
    }


}
