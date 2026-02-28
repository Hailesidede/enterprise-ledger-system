package com.example.enterprise_ledger_system.dtos;

import com.example.enterprise_ledger_system.enums.EntryType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;

public record CreateJournalRequest(

        @NotBlank
        String description,

        @NotEmpty
        List<CreateJournalLineRequest> lines
) {

    public CreateJournalRequest{

        Objects.requireNonNull(description, "Description is required");
        Objects.requireNonNull(lines, "Lines cannot be null");

        if(lines.isEmpty()) throw new IllegalArgumentException("Journal must contain at least one line");

        if(lines.size() < 2)
            throw new IllegalArgumentException("Journal must contain at least two lines");

        if(lines.size() > 1000)
            throw new IllegalArgumentException("Journal exceeds maximum allowed lines");

        validateBalanced(lines);
    }

    public static void validateBalanced(List<CreateJournalLineRequest> lines) {
        BigDecimal totalDebits = BigDecimal.ZERO;
        BigDecimal totalCredits = BigDecimal.ZERO;

        for(CreateJournalLineRequest line : lines) {
            BigDecimal amount = line.amount()
                    .setScale(4, RoundingMode.UNNECESSARY);

            if(amount.signum() <= 0){
                throw new IllegalArgumentException("Amount must be positive");
            }

            if(line.entryType() == EntryType.DEBIT){
                totalDebits = totalDebits.add(amount);
            }else{
                totalCredits = totalCredits.add(amount);
            }
        }

        if(totalDebits.compareTo(totalCredits) !=  0){
            throw new IllegalArgumentException("Journal is not balanced. Debits must equal credits");
        }
    }
}
