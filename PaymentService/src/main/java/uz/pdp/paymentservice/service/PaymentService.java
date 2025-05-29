package uz.pdp.paymentservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.pdp.clients.dtos.OrderFullDTO;
import uz.pdp.clients.dtos.OrderItemFull;
import uz.pdp.clients.dtos.OutboxStatus;
import uz.pdp.clients.dtos.PaymentCreateDTO;
import uz.pdp.clients.inventory.OutComeClient;
import uz.pdp.clients.kafkaconfig.KafkaTopics;
import uz.pdp.paymentservice.entity.Payment;
import uz.pdp.paymentservice.kafkaconfig.outbox.Outbox;
import uz.pdp.paymentservice.kafkaconfig.outbox.OutboxRepository;
import uz.pdp.paymentservice.repo.PaymentRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OutboxRepository outboxRepository;
    private final ObjectMapper objectMapper;

    @SneakyThrows
    @Transactional
    public Payment create(OrderFullDTO orderFullDTO) {
        Payment payment = new Payment(
                calculateOrderTotalPrice(orderFullDTO.getOrderItemFullList()),
                LocalDateTime.now(),
                orderFullDTO.getOrderId());
        paymentRepository.save(payment);
        outboxRepository.save(
                Outbox.builder()
                        .topic(KafkaTopics.OUTCOME_STORE)
                        .status(OutboxStatus.PENDING)
                        .payload(objectMapper.writeValueAsString(orderFullDTO))
                        .aggregateId(orderFullDTO.getOrderId())
                        .aggregateType("OrderFullDTO")
                        .build()
        );
        return payment;
    }
    private Integer calculateOrderTotalPrice(List<OrderItemFull> orderItemDTOS) {
        return orderItemDTOS.stream().mapToInt(item -> item.getQuantity() * item.getPrice()).sum();
    }

    @SneakyThrows
    public void rollbackPayment(OrderFullDTO orderFullDTO) {
        paymentRepository.deleteByOrderId(orderFullDTO.getOrderId());
        outboxRepository.save(Outbox.builder()
                .topic(KafkaTopics.PRODUCT_ROLLBACK)
                .status(OutboxStatus.PENDING)
                .aggregateId(orderFullDTO.getOrderId())
                .aggregateType("OrderFullDTO")
                .payload(objectMapper.writeValueAsString(orderFullDTO))
                .build());

    }
}
