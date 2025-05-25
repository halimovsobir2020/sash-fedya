package uz.pdp.orderservice.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.pdp.clients.dtos.OrderFullDTO;
import uz.pdp.clients.dtos.OrderItemFull;
import uz.pdp.clients.product.ProductClient;
import uz.pdp.orderservice.dto.OrderDTO;
import uz.pdp.orderservice.entity.Order;
import uz.pdp.orderservice.entity.OrderItem;
import uz.pdp.orderservice.repo.OrderItemRepository;
import uz.pdp.orderservice.repo.OrderRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductClient productClient;
    private final OrderItemRepository orderItemRepository;

    @Transactional
    public Order saveOrder(OrderDTO orderDTO) {
        Order savedOrder = orderRepository.save(new Order());
        List<OrderItem> orderItems = orderDTO.getOrderItemDTOS()
                .stream()
                .map(orderItemDTO -> new OrderItem(
                        savedOrder,
                        orderItemDTO.getProductId(),
                        orderItemDTO.getQuantity()
                )).toList();
        orderItemRepository.saveAll(orderItems);

        List<OrderItemFull> orderItemFullList = orderDTO.getOrderItemDTOS().stream().map(orderItemDTO -> new OrderItemFull(orderItemDTO.getProductId(), orderItemDTO.getQuantity())).toList();
        productClient.updateProductLeftover(new OrderFullDTO(orderItemFullList, savedOrder.getId()));

        return savedOrder;
    }

}
