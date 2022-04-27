package vkb.dto;


import lombok.*;
import vkb.entity.Goods;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.UUID;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class GoodsRequestDto implements Serializable {

    private static final long serialVersionUID = 5777870493698369073L;

    @Size(max = 200, min = 1, message = "name should not be less than 1 or greater than 200 characters in length")
    @Pattern(regexp = "^[a-zA-Z0-9 \\-,.@()/]+$", message = "invalid name pattern")
    @NotBlank(message = "name is required")
    private String name;


    private int quantity;


    private double unitPrice;

    @Size(max = 20, min = 1, message = "contact Title should not be less than 2 or greater than 5 characters in length")
    @Pattern(regexp =  "^[0-9]+$", message = "invalid contact Title")
    @NotBlank(message = "Contact Title is required")
    private String quantitySold;

    private String description;

    @NotBlank(message = "email is required")
    @Size(max =40, min =1 , message = "email should not be less than 3 or greater than 40 characters in length")
    @Pattern(regexp ="^[a-zA-Z0-9 \\-,.@()/]+$", message = "invalid email pattern")
    private String manufacturedDate;

    @Size(max = 40, min = 1, message = "merchant Physical Address should not be less than 3 or greater than 40 characters in length")
    @Pattern(regexp = "^[a-zA-Z0-9 \\-,.@()/]+$", message = "invalid merchant Physical Address")
    @NotBlank(message = "expiryDate is required")
    private String expiryDate;


    public Goods toGoods(){
        Goods goods = new Goods();
        goods.setId(UUID.randomUUID().toString());
        goods.setDescription(description);
        goods.setName(name);
        goods.setQuantity(quantity);
        goods.setQuantitySold(quantitySold);
        goods.setExpiryDate(expiryDate);
        goods.setManufacturedDate(manufacturedDate);
        goods.setUnitPrice(unitPrice);
        return goods;

    }



}
