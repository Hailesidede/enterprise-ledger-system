package com.example.enterprise_ledger_system.dtos;

import java.time.LocalDateTime;
import java.util.UUID;

public record CreateJournalResponse(
        UUID journalReference,
        String status,
        LocalDateTime createdAt,
        String apiVersion
) {
}
