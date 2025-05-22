package uz.pdp.productservice.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.productservice.entity.Category;

import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}