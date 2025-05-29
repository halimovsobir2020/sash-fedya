package uz.pdp.paymentservice.kafkaconfig.outbox;


import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.Type;
import org.hibernate.type.SqlTypes;
import uz.pdp.clients.dtos.OutboxStatus;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Outbox {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String aggregateType;
    private Long aggregateId;
    private String topic;
    @Enumerated(EnumType.STRING)
    private OutboxStatus status;
    @CreationTimestamp
    private LocalDateTime createdAt;
    private LocalDateTime sentAt;

    @Type(JsonBinaryType.class)
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "payload", columnDefinition = "jsonb")
    private String payload;

}
