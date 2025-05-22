package uz.pdp.orderservice.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.orderservice.entity.OrderItem;

import java.util.UUID;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}