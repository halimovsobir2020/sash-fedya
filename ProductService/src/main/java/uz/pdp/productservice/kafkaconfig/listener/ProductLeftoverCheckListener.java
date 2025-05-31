package uz.pdp.productservice.kafkaconfig.listener;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import uz.pdp.clients.dtos.OrderFullDTO;
import uz.pdp.productservice.service.ProductService;

import static uz.pdp.clients.kafkaconfig.KafkaTopics.PRODUCT_LEFTOVER_CHANGE;

@Component
@RequiredArgsConstructor
public class ProductLeftoverCheckListener {

    private final ProductService productService;

    @KafkaListener(topics = PRODUCT_LEFTOVER_CHANGE)
    public void consumer(OrderFullDTO orderFullDTO, Acknowledgment ack) {
        productService.updateLeftOver(orderFullDTO);
        ack.acknowledge();
    }


}
