package vkb.dto;


import lombok.*;
import vkb.entity.Cart;

import java.io.Serializable;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CartDto implements Serializable {

    private static final long serialVersionUID = 5777870493698369073L;

    private String  itemId;
    private String  storeName;
    private String  userId;
    private String  quantity;
    private String  createdDate;


    public Cart toCart(){
        Cart cart = new Cart();
        cart.setItemId(itemId);
        cart.setCreatedDate(createdDate);
        cart.setStoreName(storeName);
        cart.setQuantity(quantity);
        cart.setUserId(userId);


        return cart;

    }


}
