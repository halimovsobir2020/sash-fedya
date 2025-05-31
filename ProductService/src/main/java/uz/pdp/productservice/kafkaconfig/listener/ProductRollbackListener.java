package uz.pdp.productservice.kafkaconfig.listener;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import uz.pdp.clients.dtos.OrderFullDTO;
import uz.pdp.clients.kafkaconfig.KafkaTopics;
import uz.pdp.productservice.service.ProductService;

@Component
@RequiredArgsConstructor
public class ProductRollbackListener {

    private final ProductService productService;

    @KafkaListener(topics = KafkaTopics.PRODUCT_ROLLBACK)
    public void consumer(OrderFullDTO orderFullDTO, Acknowledgment ack) {
        productService.rollback(orderFullDTO);
        ack.acknowledge();
    }
}
