package uz.pdp.orderservice.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.pdp.orderservice.entity.abs.BaseEntity;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class OrderItem extends BaseEntity {

    @ManyToOne
    private Order order;
    private Long productId;
    private Integer quantity;
    private Integer price;
    private String productName;



}
