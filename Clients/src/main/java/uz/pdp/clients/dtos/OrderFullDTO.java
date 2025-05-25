package uz.pdp.clients.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderFullDTO {

    private List<OrderItemFull> orderItemFullList;
    private Long orderId;

}
