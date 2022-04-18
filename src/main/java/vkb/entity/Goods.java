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
@Table(name = "goods", schema = "dbo")
@AllArgsConstructor
@NoArgsConstructor
public class Goods implements Serializable {
    private static final long serialVersionUID = -5478596133621588561L;

    @Id
    @Column(name = "id")
    String id;

    @Column(name = "name")
    String name;
    @Column(name = "quantity")
    int quantity;
    @Column(name = "unit_price")
    double unitPrice;
    @Column(name = "quantity_sold")
    String quantitySold;
    @Column(name = "description")
    String description;
    @Column(name = "manufactured_date")
    String manufacturedDate;
    @Column(name = "expiry_date")
    String expiryDate;
}
