package vkb.dto;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.*;
import vkb.entity.Goods;
import vkb.util.CommonUtil;
import vkb.util.DateFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;


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

    private String description;

    @JsonDeserialize(using = DateFormat.class)
    private Date manufacturedDate;

    @JsonDeserialize(using = DateFormat.class)
    private Date expiryDate;


    public Goods toGoods(){
        Goods goods = new Goods();
        goods.setId((name.charAt(0)+"0").toUpperCase()+CommonUtil.getID());
        goods.setDescription(description);
        goods.setName(name);
        goods.setQuantity(quantity);
        goods.setExpiryDate(expiryDate);
        goods.setManufacturedDate(manufacturedDate);
        goods.setUnitPrice(unitPrice);
        return goods;

    }



}
