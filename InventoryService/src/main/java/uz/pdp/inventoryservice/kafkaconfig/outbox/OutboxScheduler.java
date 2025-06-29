package uz.pdp.inventoryservice.kafkaconfig.outbox;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OutboxScheduler {
    private final OutboxService outboxService;

    @Scheduled(initialDelay = 0, fixedRate = 5000)
    public void orderCreate() {
        outboxService.sendUnreadMessages();
    }

}
