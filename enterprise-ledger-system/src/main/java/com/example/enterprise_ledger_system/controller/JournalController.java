package com.example.enterprise_ledger_system.controller;

import com.example.enterprise_ledger_system.dtos.CreateJournalRequest;
import com.example.enterprise_ledger_system.dtos.CreateJournalResponse;
import com.example.enterprise_ledger_system.entity.IdempotencyRecord;
import com.example.enterprise_ledger_system.enums.IdempotencyStatus;
import com.example.enterprise_ledger_system.redis.RedisLockService;
import com.example.enterprise_ledger_system.services.IdempotencyService;
import com.example.enterprise_ledger_system.services.JournalService;
import com.example.enterprise_ledger_system.utils.RequestHashUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import tools.jackson.databind.ObjectMapper;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/journals")
@RequiredArgsConstructor
@Validated
public class JournalController {
    private final JournalService journalService;

    private final IdempotencyService idempotencyService;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private final RedisLockService redisLockService;

    @PostMapping
    public ResponseEntity<CreateJournalResponse> createJournal(
            @RequestHeader(name="Idempotency-Key", required = true) String idempotencyKey,
            @Valid @RequestBody CreateJournalRequest request){

        String requestHash = RequestHashUtil.sha256(request);

        boolean isNewRequest = redisLockService.acquireIdempotencyLock(idempotencyKey, requestHash);

        if(!isNewRequest){
            String existingHash = redisLockService.getExistingHash(idempotencyKey);
            if(!requestHash.equals(existingHash)){
                throw new IllegalArgumentException("Idempotency Key reused with different payload");
            }

            return ResponseEntity.status(HttpStatus.CREATED).build();
        }

        IdempotencyRecord record =
                idempotencyService.start(idempotencyKey, requestHash);

        if(record.getStatus() == IdempotencyStatus.COMPLETED){
            return ResponseEntity
                    .status(record.getHttpStatus())
                    .body(deserialize(record.getResponseBody()));
        }


        UUID journalReference = journalService.createDraftJournal(request);
         CreateJournalResponse response = new CreateJournalResponse(
                 journalReference,
                 "DRAFT",
                 LocalDateTime.now(),
                 "V1"
         );

        idempotencyService.complete(
                record,
                journalReference,
                serialize(response),
                201
        );


         return ResponseEntity
                 .created(URI.create("/api/v1/journals/"+ journalReference))
                 .body(response);
    }



    private CreateJournalResponse deserialize(String json) {
        try{
            return objectMapper.readValue(json, CreateJournalResponse.class);
        }catch (Exception e){
            throw new RuntimeException("Failed to deserialize Idempotency response ",e);
        }
    }

    private String serialize(CreateJournalResponse response) {
        try{
            return objectMapper.writeValueAsString(response);
        }catch (Exception e){
            throw new RuntimeException("Failed to serialize Idempotency response ",e);
        }
    }
}
