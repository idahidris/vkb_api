package vkb.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@Table(name = "shopping_cart", schema = "dbo")
@AllArgsConstructor
@NoArgsConstructor
public class ShoppingCart implements Serializable {
    private static final long serialVersionUID = -5478596133621588561L;


    @Id
    @Column(name = "id")
    String  id;

    @Column(name = "created_date")
    Date createdDate;

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


}
