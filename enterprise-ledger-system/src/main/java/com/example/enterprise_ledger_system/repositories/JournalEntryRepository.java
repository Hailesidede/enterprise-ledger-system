package com.example.enterprise_ledger_system.repositories;

import com.example.enterprise_ledger_system.entity.JournalEntry;
import com.example.enterprise_ledger_system.enums.JournalEntryStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface JournalEntryRepository extends JpaRepository<JournalEntry, Long> {

    Optional<JournalEntry> findByReference(UUID reference);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT j FROM JournalEntry j WHERE j.reference = :reference")
    Optional<JournalEntry> findByReferenceForUpdate(@Param("reference") UUID reference);

    Optional<JournalEntry> findByIdempotencyKey(String idempotencyKey);

    Optional<JournalEntry> findByReferenceAndStatus(UUID reference, JournalEntryStatus status);

    boolean existsByReference(UUID reference);
}
