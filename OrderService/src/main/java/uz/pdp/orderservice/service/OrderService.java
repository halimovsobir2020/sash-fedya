package uz.pdp.orderservice.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uz.pdp.clients.dtos.OrderItemFull;
import uz.pdp.clients.dtos.PaymentCreateDTO;
import uz.pdp.clients.inventory.OutComeClient;
import uz.pdp.clients.payment.PaymentClient;
import uz.pdp.clients.product.ProductClient;
import uz.pdp.orderservice.dto.OrderDTO;
import uz.pdp.orderservice.entity.Order;
import uz.pdp.orderservice.entity.OrderItem;
import uz.pdp.orderservice.entity.enums.OrderStatus;
import uz.pdp.orderservice.repo.OrderItemRepository;
import uz.pdp.orderservice.repo.OrderRepository;

import java.util.List;

@ComponentScan(basePackages = {"uz.pdp.orderservice", "uz.pdp.clients"})
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductClient productClient;
    private final OrderItemRepository orderItemRepository;
    private final PaymentClient paymentClient;
    private final OutComeClient outComeClient;

    @Transactional
    public Order createOrder(OrderDTO orderDTO) {
        Order savedOrder = orderRepository.save(new Order());
        try {
            List<OrderItemFull> orderItemFulls = checkProductAvailabilty(orderDTO);
            List<OrderItem> orderItems = orderItemFulls.stream().map(orderItem -> new OrderItem(
                    savedOrder,
                    orderItem.getProductId(),
                    orderItem.getQuantity(),
                    orderItem.getPrice(),
                    orderItem.getProductName()
            )).toList();
            orderItemRepository.saveAll(orderItems);
            Integer totalPrice = findOrderTotalPrice(orderItems);
            paymentClient.createPayment(new PaymentCreateDTO(
                    totalPrice,
                    savedOrder.getId()
            ));
            outComeClient.orderOutcome(orderDTO.getOrderItems(), savedOrder.getId());
            return savedOrder;
        } catch (Exception e) {
            paymentClient.rollbackPayment(savedOrder.getId());
            productClient.rollback(orderDTO.getOrderItems());
            outComeClient.rollback(savedOrder.getId());
            throw new RuntimeException(e);
        }
    }

    private Integer findOrderTotalPrice(List<OrderItem> orderItems) {
        return orderItems.stream().mapToInt(orderItem -> orderItem.getPrice() * orderItem.getQuantity()).sum();
    }

    private List<OrderItemFull> checkProductAvailabilty(OrderDTO orderDTO) {
        List<OrderItemFull> orderItemFulls = orderDTO.getOrderItems().stream().map(orderItemDTO -> new OrderItemFull(
                orderItemDTO.getProductId(),
                orderItemDTO.getQuantity()
        )).toList();
        ResponseEntity<List<OrderItemFull>> responseLeftOver = productClient.updateProductLeftOver(orderItemFulls);
        if (!responseLeftOver.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("not enough products");
        }
        return responseLeftOver.getBody();
    }

}

