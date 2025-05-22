package uz.pdp.productservice.dto;

import lombok.Value;

import java.util.UUID;

@Value
public class ProductDTO {

    String name;
    Integer price;
    String description;
    Long categoryId;

}
