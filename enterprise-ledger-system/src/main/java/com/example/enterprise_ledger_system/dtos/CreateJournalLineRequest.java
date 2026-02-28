package com.example.enterprise_ledger_system.dtos;

import com.example.enterprise_ledger_system.enums.EntryType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

public record CreateJournalLineRequest(
        @NotNull
        UUID accountPublicId,

        @NotNull
        EntryType entryType,

        @NotNull
        @DecimalMin(value = "0.0001", inclusive = true)
        BigDecimal amount

) {

    public CreateJournalLineRequest {
        Objects.requireNonNull(accountPublicId, "accountPublicId is required");
        Objects.requireNonNull(entryType, "entryType is required");
        Objects.requireNonNull(amount, "amount is required");

        if(amount.signum() < 0) throw new IllegalArgumentException("amount must be positive");
    }
}
