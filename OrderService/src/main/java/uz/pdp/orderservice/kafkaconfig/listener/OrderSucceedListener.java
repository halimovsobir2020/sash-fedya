package uz.pdp.orderservice.kafkaconfig.listener;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import uz.pdp.orderservice.entity.Order;
import uz.pdp.orderservice.entity.enums.OrderStatus;
import uz.pdp.orderservice.repo.OrderRepository;

import static uz.pdp.clients.kafkaconfig.KafkaTopics.ORDER_SUCCEED;

@Component
@RequiredArgsConstructor
public class OrderSucceedListener {

    private final OrderRepository orderRepository;

    @KafkaListener(topics = ORDER_SUCCEED)
    public void consumer(Long orderId, Acknowledgment ack) {
        Order order = orderRepository.findById(orderId).get();
        order.setOrderStatus(OrderStatus.CREATED);
        orderRepository.save(order);
        ack.acknowledge();
    }


}
