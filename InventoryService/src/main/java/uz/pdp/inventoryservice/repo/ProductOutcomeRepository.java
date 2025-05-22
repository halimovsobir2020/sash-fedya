package uz.pdp.inventoryservice.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.pdp.inventoryservice.entity.ProductOutcome;

import java.util.UUID;

public interface ProductOutcomeRepository extends JpaRepository<ProductOutcome, Long> {

    @Query(value = """
            select sum(amount) from product_outcome where product_id = :productId
            """, nativeQuery = true)
    Long getTotalOutcome(Long productId);

}
