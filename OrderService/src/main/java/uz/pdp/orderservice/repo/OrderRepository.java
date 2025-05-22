package uz.pdp.orderservice.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.orderservice.entity.Order;

import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, Long> {
}