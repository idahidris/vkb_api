package vkb.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


@Entity
@Data
@Table(name = "shipping_order_items", schema = "dbo")
@AllArgsConstructor
@NoArgsConstructor
public class ShippingOrderItems implements Serializable {

    private static final long serialVersionUID = -547859613621588561L;


    @ManyToOne(cascade=CascadeType.ALL , fetch=FetchType.LAZY)
    @JoinColumn(name = "batch_id", referencedColumnName="batch_id" )
    @JsonIgnore
     ShippingOrder shippingOrder;

    @Id
    @Column(name = "item_id")
    String  itemId;

    @Column(name = "unit_price")
    Long unitPrice ;

    @Column(name = "quantity")
    Double quantity;

    @Column(name = "product_name")
    String productName;


    @Column(name = "seller_name")
    String sellerName;


    @Column(name = "page_link")
    String pageLink;

    @Column(name = "image_link")
    String  imageLink;

    @Column(name = "rate")
    Double rate;

    @Column(name = "created_date")
    Date createdDate;


}
