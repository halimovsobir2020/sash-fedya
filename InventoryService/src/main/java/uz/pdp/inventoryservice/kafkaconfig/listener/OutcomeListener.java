package uz.pdp.inventoryservice.kafkaconfig.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import uz.pdp.clients.dtos.OrderFullDTO;
import uz.pdp.clients.dtos.OutboxStatus;
import uz.pdp.clients.kafkaconfig.KafkaTopics;
import uz.pdp.inventoryservice.kafkaconfig.outbox.Outbox;
import uz.pdp.inventoryservice.kafkaconfig.outbox.OutboxRepository;
import uz.pdp.inventoryservice.service.InventoryService;

@Component
@RequiredArgsConstructor
public class OutcomeListener {

    private final InventoryService inventoryService;
    private final OutboxRepository outboxRepository;
    private final ObjectMapper objectMapper;

    @SneakyThrows
    @KafkaListener(topics = KafkaTopics.OUTCOME_STORE)
    public void consumer(OrderFullDTO orderFullDTO, Acknowledgment ack) {
        try {
            inventoryService.orderUpdates(orderFullDTO);
        } catch (Exception e) {
            outboxRepository.save(
                    Outbox.builder()
                            .topic(KafkaTopics.PAYMENT_ROLLBACK)
                            .status(OutboxStatus.PENDING)
                            .payload(objectMapper.writeValueAsString(orderFullDTO))
                            .aggregateId(orderFullDTO.getOrderId())
                            .aggregateType("OrderFullDTO")
                            .build()
            );
        }
        ack.acknowledge();
    }

}
