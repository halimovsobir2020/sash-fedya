package uz.pdp.orderservice.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.orderservice.entity.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}