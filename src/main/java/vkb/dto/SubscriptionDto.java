package vkb.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;
import vkb.entity.Subscription;
import vkb.util.DateFormat;

import java.io.Serializable;
import java.util.Date;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SubscriptionDto implements Serializable {

    private static final long serialVersionUID = 5777870492698369033L;

    String  id;
    String customerId;
    String serviceType;
    String serviceTitle;
    MultipartFile documentLink;
    String description;
    double price;
    double paidAmount;
    @JsonDeserialize(using = DateFormat.class)
    Date paidAmountDate;
    String lastPaymentReference;
    @JsonDeserialize(using = DateFormat.class)
    Date subscriptionDate;
    @JsonDeserialize(using = DateFormat.class)
    Date expectedDeliveryDate;
    @JsonDeserialize(using = DateFormat.class)
    Date actualDeliveryDate;
    String status;

    public Subscription toSubscription(){
        Subscription subscription = new Subscription();
        if(this ==null)
            return null;

        subscription.setId(id);
        subscription.setCustomerId(customerId);
        subscription.setSubscriptionDate(subscriptionDate);
        subscription.setServiceType(serviceType);
        subscription.setServiceTitle(serviceTitle);
        if(documentLink !=null)subscription.setDocumentLink(documentLink.getOriginalFilename());
        subscription.setDescription(description);
        subscription.setPrice(price);
        subscription.setPaidAmount(paidAmount);
        subscription.setPaidAmountDate(paidAmountDate);
        subscription.setExpectedDeliveryDate(expectedDeliveryDate);
        subscription.setActualDeliveryDate(actualDeliveryDate);
        subscription.setStatus(status);
        subscription.setLastPaymentReference(lastPaymentReference);

        return subscription;
    }
}
