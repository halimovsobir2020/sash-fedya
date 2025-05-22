package uz.pdp.productservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.pdp.productservice.entity.abs.BaseEntity;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Product extends BaseEntity {

    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private Integer price;
    private String description;
    @ManyToOne
    private Category category;
    private Long attachmentId;
    private Integer leftOver = 0;

    @Version
    private Integer version;

}
