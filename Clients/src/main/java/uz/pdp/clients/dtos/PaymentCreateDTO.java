package uz.pdp.clients.dtos;

import lombok.Value;

@Value
public class PaymentCreateDTO {
    Integer amount;
    Long orderId;
}
