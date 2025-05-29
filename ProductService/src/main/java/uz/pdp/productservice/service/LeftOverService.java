package uz.pdp.productservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.pdp.clients.dtos.OrderFullDTO;
import uz.pdp.clients.dtos.OrderItemFull;
import uz.pdp.clients.dtos.OutboxStatus;
import uz.pdp.clients.kafkaconfig.KafkaTopics;
import uz.pdp.clients.payment.PaymentClient;
import uz.pdp.productservice.kafkaconfig.outbox.Outbox;
import uz.pdp.productservice.entity.Product;
import uz.pdp.productservice.kafkaconfig.outbox.OutboxRepository;
import uz.pdp.productservice.repo.ProductRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LeftOverService {
    private final ProductRepository productRepository;
    private final PaymentClient paymentClient;
    private final OutboxRepository outboxRepository;
    private final ObjectMapper objectMapper;

    @SneakyThrows
    @Transactional
    public void updateLeftOverStep2(OrderFullDTO orderFullDTO) {
        List<Long> productIds = orderFullDTO.getOrderItemFullList().stream().map(OrderItemFull::getProductId).toList();
        List<Product> products = productRepository.findAllByIdIn(productIds);
        for (OrderItemFull orderItemDTO : orderFullDTO.getOrderItemFullList()) {
            Product product = products.stream().filter((item) -> item.getId().equals(orderItemDTO.getProductId())).findFirst().orElseThrow();
            if (product.getLeftOver() < orderItemDTO.getQuantity()) {
                throw new RuntimeException("Not enough left over");
            }
            product.setLeftOver(product.getLeftOver() - orderItemDTO.getQuantity());
            orderItemDTO.setPrice(product.getPrice());
            orderItemDTO.setProductName(product.getName());
        }
        productRepository.saveAll(products);
        outboxRepository.save(Outbox.builder()
                .topic(KafkaTopics.CREATE_ORDER_PAYMENT)
                .status(OutboxStatus.PENDING)
                .payload(objectMapper.writeValueAsString(orderFullDTO))
                .aggregateId(orderFullDTO.getOrderId())
                .aggregateType("OrderFullDTO")
                .build());
    }


    @Transactional
    public void compensateActionStep2(OrderFullDTO orderFullDTO) {
        List<Long> productIds = orderFullDTO.getOrderItemFullList().stream().map(OrderItemFull::getProductId).toList();
        List<Product> products = productRepository.findAllByIdIn(productIds);
        for (OrderItemFull orderItemDTO : orderFullDTO.getOrderItemFullList()) {
            Product product = products.stream().filter(item -> item.getId().equals(orderItemDTO.getProductId())).findFirst().orElseThrow();
            product.setLeftOver(product.getLeftOver() + orderItemDTO.getQuantity());
        }
        productRepository.saveAll(products);
        outboxRepository.save(
                Outbox.builder()
                        .topic(KafkaTopics.ORDER_ROLLBACK)
                        .status(OutboxStatus.PENDING)
                        .payload(orderFullDTO.getOrderId().toString())
                        .aggregateId(orderFullDTO.getOrderId())
                        .aggregateType("Long")
                        .build()
        );

    }

}
