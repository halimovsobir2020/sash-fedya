package uz.pdp.inventoryservice.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import uz.pdp.inventoryservice.entity.ProductOutcome;

public interface ProductOutcomeRepository extends JpaRepository<ProductOutcome, Long> {


    @Transactional
    void deleteAllByOrderId(Long orderId);

}