package uz.pdp.paymentservice.repo;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import uz.pdp.paymentservice.entity.Payment;

import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    @Transactional
    void deleteByOrderId(Long orderId);
}