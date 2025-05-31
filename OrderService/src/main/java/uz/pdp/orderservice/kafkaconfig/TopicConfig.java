package uz.pdp.orderservice.kafkaconfig;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static uz.pdp.clients.kafkaconfig.KafkaTopics.PRODUCT_LEFTOVER_CHANGE;


@Configuration
public class TopicConfig {

    @Bean
    public NewTopic createTopic() {
        return new NewTopic(PRODUCT_LEFTOVER_CHANGE, 1, (short) 1);
    }

}
