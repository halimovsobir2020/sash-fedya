package uz.pdp.paymentservice.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.paymentservice.entity.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    void deleteByOrderId(Long orderId);

}