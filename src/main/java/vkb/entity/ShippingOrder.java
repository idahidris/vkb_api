package vkb.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Data
@Table(name = "shipping_order", schema = "dbo")
@AllArgsConstructor
@NoArgsConstructor
public class ShippingOrder implements Serializable {

    private static final long serialVersionUID = -5478513621588561L;

    @Id
    @Column(name = "batch_id")
    String  batchId;

    @Column(name = "shipping_vendor")
    String  shippingVendor;

    @Column(name = "shipping_vendor_email")
    String shippingVendorEmail ;

    @Column(name = "description")
    String description ;

    @Column(name = "order_date")
    Date orderDate;

    @Column(name = "status")
    String status;

    @OneToMany(mappedBy = "shippingOrder")
    List<ShippingOrderItems> shippingOrderItems;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ShippingOrder)) return false;
        ShippingOrder that = (ShippingOrder) o;
        return Objects.equals(batchId, that.batchId);

    }

    @Override
    public int hashCode() {
        return Objects.hash(batchId);
    }
}
