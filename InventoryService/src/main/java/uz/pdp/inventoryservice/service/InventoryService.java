package uz.pdp.inventoryservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.pdp.clients.dtos.OrderFullDTO;
import uz.pdp.clients.dtos.OutboxStatus;
import uz.pdp.inventoryservice.entity.ProductOutcome;
import uz.pdp.inventoryservice.kafkaconfig.outbox.Outbox;
import uz.pdp.inventoryservice.kafkaconfig.outbox.OutboxRepository;
import uz.pdp.inventoryservice.repo.ProductOutcomeRepository;

import java.time.LocalDateTime;
import java.util.List;

import static uz.pdp.clients.kafkaconfig.KafkaTopics.ORDER_SUCCEED;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final ProductOutcomeRepository productOutcomeRepository;
    private final OutboxRepository outboxRepository;

    @SneakyThrows
    @Transactional
    public void orderUpdates(OrderFullDTO orderFullDTO) {
        List<ProductOutcome> list = orderFullDTO.getOrderItemFullList().stream().map(item -> new ProductOutcome(item.getProductId(), item.getQuantity(), LocalDateTime.now(), orderFullDTO.getOrderId())).toList();
        productOutcomeRepository.saveAll(list);
        outboxRepository.save(
                Outbox.builder()
                        .topic(ORDER_SUCCEED)
                        .status(OutboxStatus.PENDING)
                        .payload(orderFullDTO.getOrderId().toString())
                        .aggregateId(orderFullDTO.getOrderId())
                        .aggregateType("Long")
                        .build()
        );
    }

    @Transactional
    public void rollback(Long orderId) {
        productOutcomeRepository.deleteAllByOrderId(orderId);
    }
}
