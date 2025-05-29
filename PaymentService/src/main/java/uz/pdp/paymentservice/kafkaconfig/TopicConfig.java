package uz.pdp.paymentservice.kafkaconfig;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uz.pdp.clients.kafkaconfig.KafkaTopics;

@Configuration
public class TopicConfig {

    @Bean
    public NewTopic createTopic(){
        return new NewTopic(KafkaTopics.PRODUCT_ROLLBACK, 1, (short) 1);
    }

    @Bean
    public NewTopic createTopic2(){
        return new NewTopic(KafkaTopics.OUTCOME_STORE, 1, (short) 1);
    }

}
