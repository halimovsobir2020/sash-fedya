package uz.pdp.paymentservice.kafkaconfig.listener;


import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import uz.pdp.clients.dtos.OrderFullDTO;
import uz.pdp.clients.kafkaconfig.KafkaTopics;
import uz.pdp.paymentservice.service.PaymentService;

@Component
@RequiredArgsConstructor
public class PaymentRollbackListener {
    private final PaymentService paymentService;

    @SneakyThrows
    @KafkaListener(topics = KafkaTopics.PAYMENT_ROLLBACK)
    public void consumer(OrderFullDTO orderFullDTO, Acknowledgment ack) {
        paymentService.rollbackPayment(orderFullDTO);
        ack.acknowledge();
    }
}
