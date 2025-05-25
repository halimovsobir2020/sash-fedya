package uz.pdp.paymentservice.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.pdp.clients.dtos.PaymentCreateDTO;
import uz.pdp.paymentservice.entity.Payment;
import uz.pdp.paymentservice.repo.PaymentRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;

    @Transactional
    public Payment savePayment(PaymentCreateDTO paymentCreateDTO) {
        Payment payment = new Payment(
                paymentCreateDTO.getTotalPrice(),
                LocalDateTime.now(),
                paymentCreateDTO.getOrderId());

        return paymentRepository.save(payment);
    }

    public void rollbackPayment(Long orderId) {
        paymentRepository.deleteByOrderId(orderId);
    }
}
