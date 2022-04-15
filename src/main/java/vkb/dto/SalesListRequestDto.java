//package vkb.dto;
//
//
//import lombok.*;
//import vkb.entity.Goods;
//import vkb.entity.Sales;
//
//import javax.validation.constraints.NotBlank;
//import javax.validation.constraints.Pattern;
//import javax.validation.constraints.Size;
//import java.io.Serializable;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//import java.util.UUID;
//
//
//@Data
//@Builder
//@NoArgsConstructor
//@AllArgsConstructor
//@ToString
//public class SalesListRequestDto implements Serializable {
//
//    private static final long serialVersionUID = 5777870493698369073L;
//
//    List<CartDto> cartDtoList;
//
//    @Size(max = 50, min = 1, message = "customerRef should not be less than 1 or greater than 50 characters in length")
//    @Pattern(regexp = "^[a-zA-Z0-9 \\-,.@()/]+$", message = "invalid customerRef pattern")
//    @NotBlank(message = "name is required")
//    String customerRef;
//
//
//    public List<Sales> toSales(){
//        List<Sales> result = new ArrayList<>();
//
//        for (CartDto cartDto : cartDtoList){
//            Goods item = cartDto.getGoods();
//            Sales sales = new Sales();
//
//            sales.setSalesId(UUID.randomUUID().toString());
//            sales.setCustomerRef(customerRef);
//            sales.setExpiryDate(item.getExpiryDate());
//            sales.setItemDescription(item.getDescription());
//            sales.setItemId(item.getId());
//            sales.setItemName(item.getName());
//            sales.setQuantity(cartDto.getQuantity());
//            sales.setUnitPrice(item.getUnitPrice());
//            sales.setSalesDate(new Date().toString());
//            sales.setManufacturedDate(item.getManufacturedDate());
//            sales.setTotalPrice(""+(Double.parseDouble(item.getUnitPrice()) * Double.parseDouble(cartDto.getQuantity())));
//            result.add(sales);
//
//
//        }
//
//        return result;
//
//    }
//
//
//
//}
