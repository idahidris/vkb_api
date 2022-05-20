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

    @Column(name = "description")
    String description;

    @JsonDeserialize(using = DateFormat.class)
    @Column(name = "manufactured_date")
    Date manufacturedDate;

    @JsonDeserialize(using = DateFormat.class)
    @Column(name = "expiry_date")
    Date expiryDate;
}
