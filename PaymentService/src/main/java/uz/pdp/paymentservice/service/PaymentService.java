package uz.pdp.paymentservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.pdp.clients.dtos.PaymentCreateDTO;
import uz.pdp.paymentservice.entity.Payment;
import uz.pdp.paymentservice.repo.PaymentRepository;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public Payment create(PaymentCreateDTO paymentCreateDTO) {
        Payment payment = new Payment();
        payment.setAmount(paymentCreateDTO.getAmount());
        payment.setOrderId(paymentCreateDTO.getOrderId());
        return paymentRepository.save(payment);
    }

    public void rollbackPayment(Long orderId) {
        paymentRepository.deleteByOrderId(orderId);
    }
}
