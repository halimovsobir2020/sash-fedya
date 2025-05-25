package uz.pdp.clients.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemFull {

    private Long productId;
    private Integer quantity;
    private Integer price;
    private String productName;


    public OrderItemFull(Long productId, Integer quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }
}
