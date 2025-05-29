package uz.pdp.orderservice.kafkaconfig.listener;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import uz.pdp.orderservice.service.OrderService;

import static uz.pdp.clients.kafkaconfig.KafkaTopics.ORDER_ROLLBACK;

@Component
@RequiredArgsConstructor
public class OrderRollbackListener {

    private final OrderService orderService;

    @KafkaListener(topics = ORDER_ROLLBACK)
    public void consumer(Long orderId, Acknowledgment ack) {
        orderService.rollback(orderId);
        ack.acknowledge();
    }

}
