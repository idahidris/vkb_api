package vkb.entity;

import lombok.Data;

import java.io.Serializable;


@Data
public class ShippingOrderItemsId implements Serializable {
    ShippingOrder shippingOrder;
    String  itemId;
}
