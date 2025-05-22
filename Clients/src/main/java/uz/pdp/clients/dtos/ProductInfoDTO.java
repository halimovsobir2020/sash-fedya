package uz.pdp.clients.dtos;

import lombok.Value;

@Value
public class ProductInfoDTO {
    Long id;
    String name;
    Integer price;
}
