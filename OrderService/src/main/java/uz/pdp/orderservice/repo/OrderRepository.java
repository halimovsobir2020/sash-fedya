package uz.pdp.orderservice.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.orderservice.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
}