package com.example.enterprise_ledger_system.entity;

import com.example.enterprise_ledger_system.enums.IdempotencyStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(
        name="idempotency_records",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_idempotency_key",
                        columnNames = "idempotency_key"
                )
        },
        indexes = {
                @Index(name="idx_idempotency_expires_at", columnList="expires_at"),
                @Index(name= "idex_idempotency_resource", columnList = "resource_reference")
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class IdempotencyRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "idempotency_key", nullable = false, length = 100, updatable = false)
    private String idempotencyKey;

    @Column(name = "request_hash", nullable = false, length = 64, updatable = false)
    private String requestHash;

    @Column(name= "resource_reference", updatable = false)
    private UUID resourceReference;


    @Lob
    @Column(name = "response_body", columnDefinition = "TEXT")
    private String responseBody;

    @Column(name ="http_status")
    private Integer httpStatus;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private IdempotencyStatus status;

    @Column(name="created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name="expires_at", nullable = false)
    private LocalDateTime expiresAt;

    @Version
    private Long version;

    public static IdempotencyRecord startProcessing(
            String idempotencyKey,
            String requestHash,
            int ttlHours
    ){
        if(idempotencyKey == null || idempotencyKey.isBlank())
            throw new IllegalArgumentException("IdempotencyKey is required");

        if(requestHash == null || requestHash.length() != 64)
            throw new IllegalArgumentException("Invalid request hash");

        IdempotencyRecord record = new IdempotencyRecord();
        record.idempotencyKey = idempotencyKey;
        record.requestHash = requestHash;
        record.status = IdempotencyStatus.PROCESSING;
        record.createdAt = LocalDateTime.now();
        record.expiresAt = LocalDateTime.now().plusHours(ttlHours);

        return record;
    }

    public void markCompleted(UUID resourceReference,
                              String responseBody,
                              int httpStatus){
        if(this.status == IdempotencyStatus.COMPLETED)
            throw new IllegalArgumentException("IdempotencyStatus is already COMPLETED");

        this.resourceReference = resourceReference;
        this.responseBody = responseBody;
        this.httpStatus = httpStatus;
        this.status = IdempotencyStatus.COMPLETED;
    }

    public void markFailed(int httpStatus){
        this.status = IdempotencyStatus.FAILED;
        this.httpStatus = httpStatus;
    }

    public boolean isExpired(){
        return  LocalDateTime.now().isAfter(expiresAt);
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(!(o instanceof IdempotencyRecord)) return false;
        IdempotencyRecord that = (IdempotencyRecord) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
