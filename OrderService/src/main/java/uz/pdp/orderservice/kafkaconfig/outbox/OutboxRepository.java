package uz.pdp.orderservice.kafkaconfig.outbox;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.clients.dtos.OutboxStatus;

import java.util.List;
import java.util.UUID;

public interface OutboxRepository extends JpaRepository<Outbox, UUID> {

    List<Outbox> findAllByStatus(OutboxStatus status);

}