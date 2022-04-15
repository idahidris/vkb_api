package vkb.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Data
@Table(name = "cart", schema = "dbo")
@AllArgsConstructor
@NoArgsConstructor
public class Cart implements Serializable {
    private static final long serialVersionUID = -5478596133621588561L;

    @Id
    @Column(name = "item_id")
    String itemId;
    @Column(name = "store_name")
    String storeName;
    @Column(name = "user_id")
    String userId;
    @Column(name = "quantity")
    String quantity;
    @Column(name = "created_date")
    String createdDate;


}
