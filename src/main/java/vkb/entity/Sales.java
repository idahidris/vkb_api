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
@Table(name = "sales", schema = "dbo")
@AllArgsConstructor
@NoArgsConstructor
public class Sales implements Serializable {
    private static final long serialVersionUID = -5478596133621588561L;

    @Column(name = "store_name")
    String storeName;

    @Id
    @Column(name = "id")
    String  salesId;
    @Column(name = "item_name")
    String itemName;
    @Column(name = "quantity")
    String quantity;
    @Column(name = "unit_price")
    String unitPrice;
    @Column(name = "total_price")
    String totalPrice;
    @Column(name = "item_description")
    String itemDescription;
    @JsonDeserialize(using = DateFormat.class)
    @Column(name = "manufactured_date")
    Date manufacturedDate;
    @JsonDeserialize(using = DateFormat.class)
    @Column(name = "expiry_date")
    Date expiryDate;
    @Column(name = "customer_ref")
    String customerRef;
    @JsonDeserialize(using = DateFormat.class)
    @Column(name = "sales_date")
    Date salesDate;
    @Column(name = "item_id")
    String itemId;
    @Column(name = "batch_id")
    String batchId;


}
