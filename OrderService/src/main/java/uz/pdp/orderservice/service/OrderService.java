package uz.pdp.orderservice.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uz.pdp.clients.dtos.PaymentCreateDTO;
import uz.pdp.clients.dtos.ProductInfoDTO;
import uz.pdp.clients.inventory.OutComeClient;
import uz.pdp.clients.payment.PaymentClient;
import uz.pdp.clients.product.ProductClient;
import uz.pdp.clients.product.RollBackProductLeftOverClient;
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
    private final RollBackProductLeftOverClient rollBackProductLeftOverClient;
    private final OutComeClient outComeClient;

    @Transactional
    public Order createOrder(OrderDTO orderDTO) {
        List<ProductInfoDTO> productInfoDTOS = checkProductAvailabilty(orderDTO);
        Order savedOrder = orderRepository.save(new Order());
        List<OrderItem> orderItems = orderDTO.getOrderItems().stream().map(orderItem -> new OrderItem(
                savedOrder,
                orderItem.getProductId(),
                orderItem.getQuantity(),
                findProductPrice(productInfoDTOS, orderItem.getProductId()),
                findProductName(productInfoDTOS, orderItem.getProductId())
        )).toList();
        orderItemRepository.saveAll(orderItems);
        Integer totalPrice = findOrderTotalPrice(orderItems);
        ResponseEntity<?> paymentResponse = paymentClient.createPayment(new PaymentCreateDTO(
                totalPrice,
                savedOrder.getId()
        ));
        if(!paymentResponse.getStatusCode().is2xxSuccessful()){
            rollBackProductLeftOverClient.rollback(orderDTO.getOrderItems());
            throw new RuntimeException("Payment failed");
        }
        ResponseEntity<?> responseProductOutcome = outComeClient.orderOutcome(orderDTO.getOrderItems(), savedOrder.getId());
        if(!responseProductOutcome.getStatusCode().is2xxSuccessful()){
            rollBackProductLeftOverClient.rollback(orderDTO.getOrderItems());
            throw new RuntimeException("error outcome");
        }
        savedOrder.setOrderStatus(OrderStatus.CREATED);
        return orderRepository.save(savedOrder);
    }

    private Integer findOrderTotalPrice(List<OrderItem> orderItems) {
        return orderItems.stream().mapToInt(orderItem -> orderItem.getPrice()*orderItem.getQuantity()).sum();
    }

    @SuppressWarnings("unchecked")
    private List<ProductInfoDTO> checkProductAvailabilty(OrderDTO orderDTO) {
        ResponseEntity<?> responseLeftOver = productClient.updateProductLeftOver(orderDTO.getOrderItems());
        if (!responseLeftOver.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("not enough products");
        }
        return (List<ProductInfoDTO>) responseLeftOver.getBody();
    }

    private Integer findProductPrice(List<ProductInfoDTO> productInfoDTOS, Long productId) {
        ProductInfoDTO productInfoDTO = productInfoDTOS.stream()
                .filter(item -> item.getId().equals(productId))
                .findFirst().orElseThrow();
        return productInfoDTO.getPrice();
    }

    private String findProductName(List<ProductInfoDTO> productInfoDTOS, Long productId) {
        ProductInfoDTO productInfoDTO = productInfoDTOS.stream()
                .filter(item -> item.getId().equals(productId))
                .findFirst().orElseThrow();
        return productInfoDTO.getName();
    }
}
