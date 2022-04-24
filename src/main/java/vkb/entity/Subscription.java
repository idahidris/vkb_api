package vkb.entity;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vkb.util.DateFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@Table(name = "subscription", schema = "dbo")
@AllArgsConstructor
@NoArgsConstructor
public class Subscription implements Serializable {
    private static final long serialVersionUID = -5478596133621588561L;

    @Id
    @Column(name = "id")
    String  id;
    @Column(name = "customer_id")
    String customerId;
    @Column(name = "service_type")
    String serviceType;
    @Column(name = "service_title")
    String serviceTitle;
    @Column(name = "document_link")
    String documentLink;
    @Column(name = "description")
    String description;
    @Column(name = "price")
    double price;
    @Column(name = "paid_amount")
    double paidAmount;

    @Column(name = "paid_amount_date")
    @JsonDeserialize(using = DateFormat.class)
    Date paidAmountDate;

    @Column(name = "last_payment_reference")
    String lastPaymentReference;

    @JsonDeserialize(using = DateFormat.class)
    @Column(name = "subscription_date")
    Date subscriptionDate;

    @JsonDeserialize(using = DateFormat.class)
    @Column(name = "expected_delivery_date")
    Date expectedDeliveryDate;

    @JsonDeserialize(using = DateFormat.class)
    @Column(name = "actual_delivery_date")
    Date actualDeliveryDate;

    @Column(name = "status")
    String status;

}
