package uz.pdp.paymentservice.kafkaconfig.outbox;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import uz.pdp.clients.dtos.OutboxStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static uz.pdp.clients.dtos.TypeMapper.typeMapper;

@Slf4j
@Service
@RequiredArgsConstructor
public class OutboxService {
    private final OutboxRepository outboxRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @SneakyThrows
    public void sendUnreadMessages() {
        List<Outbox> notReadMessages = outboxRepository.findAllByStatus(OutboxStatus.PENDING);
        List<Outbox> successList = new ArrayList<>();
        for (Outbox outbox : notReadMessages) {
            try {
                Object o = objectMapper.readValue(outbox.getPayload(), typeMapper.get(outbox.getAggregateType()));
                kafkaTemplate.send(outbox.getTopic(), o).get();
                outbox.setStatus(OutboxStatus.SENT);
                successList.add(outbox);
            } catch (InterruptedException | ExecutionException e) {
                log.error("Failed to send outbox event id={} to Kafka", outbox.getId(), e);
            }
        }
        if (!successList.isEmpty()) {
            outboxRepository.saveAll(successList);
        }
    }

}
