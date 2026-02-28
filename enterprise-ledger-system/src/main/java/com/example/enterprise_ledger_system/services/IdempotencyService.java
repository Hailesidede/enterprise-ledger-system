package com.example.enterprise_ledger_system.services;

import com.example.enterprise_ledger_system.entity.IdempotencyRecord;
import com.example.enterprise_ledger_system.repositories.IdempotencyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class IdempotencyService {

    private final IdempotencyRepository idempotencyRepository;

    @Transactional
    public IdempotencyRecord start(String key, String requestHash) {
        return idempotencyRepository.findByIdempotencyKey(key)
                .map(existing ->{
                    if(!existing.getRequestHash().equals(requestHash)){
                        throw new IllegalArgumentException("Idempotency key reused with different payload");
                    }
                    return existing;
                })
                .orElseGet(()->{
                    try{
                        IdempotencyRecord record =
                                IdempotencyRecord.startProcessing(key, requestHash, 24);
                        return idempotencyRepository.save(record);
                    }catch(DataIntegrityViolationException e){
                        return idempotencyRepository.findByIdempotencyKey(key)
                                .orElseThrow();
                    }
                });
    }

    @Transactional
    public void complete(IdempotencyRecord record,
                         UUID resourceReference,
                         String responseBody,
                         int httpStatus) {
        record.markCompleted(resourceReference, responseBody, httpStatus);
        idempotencyRepository.save(record);
    }
}
