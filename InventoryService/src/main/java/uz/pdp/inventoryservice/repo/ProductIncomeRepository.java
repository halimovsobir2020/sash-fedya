package uz.pdp.inventoryservice.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.inventoryservice.entity.ProductIncome;

public interface ProductIncomeRepository extends JpaRepository<ProductIncome, Long> {
}