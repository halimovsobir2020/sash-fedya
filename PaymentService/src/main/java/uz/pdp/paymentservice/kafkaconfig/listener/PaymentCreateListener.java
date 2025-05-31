package uz.pdp.paymentservice.kafkaconfig.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import uz.pdp.clients.dtos.OrderFullDTO;
import uz.pdp.clients.dtos.OutboxStatus;
import uz.pdp.clients.kafkaconfig.KafkaTopics;
import uz.pdp.paymentservice.kafkaconfig.outbox.Outbox;
import uz.pdp.paymentservice.kafkaconfig.outbox.OutboxRepository;
import uz.pdp.paymentservice.service.PaymentService;

@Component
@RequiredArgsConstructor
public class PaymentCreateListener {

    private final PaymentService paymentService;
    private final OutboxRepository outboxRepository;
    private final ObjectMapper objectMapper;

    @SneakyThrows
    @KafkaListener(topics = KafkaTopics.CREATE_ORDER_PAYMENT)
    public void consumer(OrderFullDTO orderFullDTO, Acknowledgment ack) {
        try {
            paymentService.create(orderFullDTO);
        } catch (Exception e) {
            outboxRepository.save(Outbox.builder()
                    .topic(KafkaTopics.PRODUCT_ROLLBACK)
                    .status(OutboxStatus.PENDING)
                    .aggregateId(orderFullDTO.getOrderId())
                    .aggregateType("OrderFullDTO")
                    .payload(objectMapper.writeValueAsString(orderFullDTO))
                    .build());
        }
        ack.acknowledge();
    }

}
