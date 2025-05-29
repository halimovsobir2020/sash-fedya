package uz.pdp.orderservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;
import uz.pdp.clients.dtos.OrderFullDTO;
import uz.pdp.clients.dtos.OrderItemFull;
import uz.pdp.clients.dtos.OutboxStatus;
import uz.pdp.orderservice.dto.OrderDTO;
import uz.pdp.orderservice.entity.Order;
import uz.pdp.orderservice.entity.OrderItem;
import uz.pdp.orderservice.kafkaconfig.outbox.Outbox;
import uz.pdp.orderservice.entity.enums.OrderStatus;
import uz.pdp.orderservice.repo.OrderItemRepository;
import uz.pdp.orderservice.repo.OrderRepository;
import uz.pdp.orderservice.kafkaconfig.outbox.OutboxRepository;

import java.util.List;

import static uz.pdp.clients.kafkaconfig.KafkaTopics.PRODUCT_LEFTOVER_CHANGE;

@ComponentScan(basePackages = {"uz.pdp.orderservice", "uz.pdp.clients"})
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final OutboxRepository outboxRepository;
    private final ObjectMapper objectMapper;

    @SneakyThrows
    @Transactional
    public Order createOrder(OrderDTO orderDTO) {
        Order savedOrder = orderRepository.save(new Order());
        List<OrderItem> orderItems = orderDTO.getOrderItems()
                .stream()
                .map(orderItemDTO -> new OrderItem(
                        savedOrder,
                        orderItemDTO.getProductId(),
                        orderItemDTO.getQuantity()
                )).toList();
        orderItemRepository.saveAll(orderItems);

        List<OrderItemFull> orderItemFullList = orderDTO.getOrderItems().stream().map(orderItemDTO -> new OrderItemFull(orderItemDTO.getProductId(), orderItemDTO.getQuantity())).toList();
        OrderFullDTO orderFullDTO = new OrderFullDTO(orderItemFullList, savedOrder.getId());

        outboxRepository.save(Outbox.builder()
                .topic(PRODUCT_LEFTOVER_CHANGE)
                .status(OutboxStatus.PENDING)
                .aggregateId(savedOrder.getId())
                .aggregateType("OrderFullDTO")
                .payload(objectMapper.writeValueAsString(orderFullDTO))
                .build());
        return savedOrder;
    }

    public void rollback(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow();
        order.setOrderStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);
    }

}

