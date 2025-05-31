package uz.pdp.productservice.kafkaconfig;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uz.pdp.clients.kafkaconfig.KafkaTopics;

@Configuration
public class TopicConfig {

    @Bean
    public NewTopic createTopic(){
        return new NewTopic(KafkaTopics.CREATE_ORDER_PAYMENT, 1, (short) 1);
    }

}
