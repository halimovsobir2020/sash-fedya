package uz.pdp.productservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.pdp.productservice.entity.abs.BaseEntity;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Category extends BaseEntity {
    @Column(nullable = false)
    private String name;
}
