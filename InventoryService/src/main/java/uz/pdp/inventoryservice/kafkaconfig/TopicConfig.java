package uz.pdp.inventoryservice.kafkaconfig;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uz.pdp.clients.kafkaconfig.KafkaTopics;

import static uz.pdp.clients.kafkaconfig.KafkaTopics.ORDER_SUCCEED;
import static uz.pdp.clients.kafkaconfig.KafkaTopics.PAYMENT_ROLLBACK;

@Configuration
public class TopicConfig {

    @Bean
    public NewTopic createTopic(){
        return new NewTopic(PAYMENT_ROLLBACK, 1, (short) 1);
    }

    @Bean
    public NewTopic createTopic2(){
        return new NewTopic(ORDER_SUCCEED, 1, (short) 1);
    }

}
