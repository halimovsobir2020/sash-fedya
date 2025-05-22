package uz.pdp.paymentservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import uz.pdp.paymentservice.entity.abs.BaseEntity;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Payment extends BaseEntity {

    @Column(nullable = false)
    private Integer amount;
    @CreationTimestamp
    private LocalDateTime paymentDateTime;
    private Long orderId;

}
