package uz.pdp.orderservice.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uz.pdp.clients.dtos.OrderItemDTO;
import uz.pdp.clients.dtos.OrderItemFull;
import uz.pdp.clients.dtos.PaymentCreateDTO;
import uz.pdp.clients.inventory.InventoryClient;
import uz.pdp.clients.payment.PaymentClient;
import uz.pdp.clients.product.ProductClient;
import uz.pdp.orderservice.dto.OrderDTO;
import uz.pdp.orderservice.entity.Order;
import uz.pdp.orderservice.entity.OrderItem;
import uz.pdp.orderservice.entity.enums.OrderStatus;
import uz.pdp.orderservice.repo.OrderItemRepository;
import uz.pdp.orderservice.repo.OrderRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductClient productClient;
    private final OrderItemRepository orderItemRepository;
    private final PaymentClient paymentClient;
    private final InventoryClient inventoryClient;

    @Transactional
    public Order saveOrder(OrderDTO orderDTO) {
        Order savedOrder = orderRepository.save(new Order());
        try {
            ResponseEntity<List<OrderItemFull>> responseLeftover = productClient.updateProductLeftover(orderDTO.getOrderItemDTOS());
            List<OrderItemFull> orderItemDTOS = responseLeftover.getBody();
            if (orderItemDTOS == null)
                throw new RuntimeException("Order is not created");

            List<OrderItem> orderItems = orderItemDTOS
                    .stream()
                    .map(orderItemDTO -> new OrderItem(
                            savedOrder,
                            orderItemDTO.getProductId(),
                            orderItemDTO.getQuantity(),
                            orderItemDTO.getPrice(),
                            orderItemDTO.getProductName()
                    )).toList();

            orderItemRepository.saveAll(orderItems);
            Integer totalPrice = calculateOrderTotalPrice(orderItemDTOS);
            paymentClient.savePayment(new PaymentCreateDTO(totalPrice, savedOrder.getId()));
            inventoryClient.orderOutcome(orderDTO.getOrderItemDTOS(), savedOrder.getId());
            return savedOrder;
        } catch (Exception e) {
            productClient.rollbackProductLeftover(orderDTO.getOrderItemDTOS());
            paymentClient.rollbackPayment(savedOrder.getId());
            inventoryClient.rollbackOutcome(savedOrder.getId());
            throw new RuntimeException(e);
        }
    }

    private Integer calculateOrderTotalPrice(List<OrderItemFull> orderItemDTOS) {
        return orderItemDTOS.stream().mapToInt(item -> item.getQuantity() * item.getPrice()).sum();
    }
}
