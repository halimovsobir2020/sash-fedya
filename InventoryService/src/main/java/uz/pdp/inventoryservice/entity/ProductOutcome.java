package uz.pdp.inventoryservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.pdp.inventoryservice.entity.abs.BaseEntity;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ProductOutcome extends BaseEntity {

    @Column(nullable = false)
    private Long productId;
    @Column(nullable = false)
    private Integer amount;
    private LocalDateTime incomeDataTime;
    private Long orderId;

}
