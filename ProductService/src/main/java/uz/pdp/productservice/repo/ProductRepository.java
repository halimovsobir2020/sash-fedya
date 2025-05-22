package uz.pdp.productservice.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.productservice.entity.Product;

import java.util.List;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findAllByIdIn(List<Long> ids);

}