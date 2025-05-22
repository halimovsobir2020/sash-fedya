package uz.pdp.paymentservice.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.paymentservice.entity.Payment;

import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}