package uz.pdp.inventoryservice.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.pdp.inventoryservice.entity.ProductIncome;

import java.util.UUID;

public interface ProductIncomeRepository extends JpaRepository<ProductIncome, Long> {
    @Query(value = """
            select sum(amount) from product_income where product_id = :productId
            """, nativeQuery = true)
    Long getTotalIncome(Long productId);
}