package com.example.enterprise_ledger_system.entity;

import com.example.enterprise_ledger_system.enums.EntryType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(
        name = "JOURNAL_LINES",
        indexes = {
                @Index(name = "indx_journal_line_journal", columnList= "journal_entry_id"),
                @Index( name = "idx_journal_line_account", columnList = "account_id")
        }
)
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class JournalLine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "JOURNAL_ENTRY_ID", nullable = false)
    private JournalEntry journalEntry;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ACCOUNT_ID", nullable = false)
    private Account account;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EntryType entryType;

    @Column(nullable = false, precision = 19, scale = 4, updatable = false)
    private BigDecimal amount;

    @Column(length = 3, nullable = false, updatable = false)
    private String currency;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Version
    private Long version;

    public static JournalLine create(
            JournalEntry journalEntry,
            Account account,
            EntryType entryType,
            BigDecimal amount,
            String currency
    ){
        if(journalEntry == null)
            throw  new IllegalArgumentException("journalEntry is required");


        if(entryType == null)
            throw  new IllegalArgumentException("entryType is required");


        if(amount == null || amount.compareTo(BigDecimal.ZERO) < 0)
            throw  new IllegalArgumentException("amount must be positive");


        if(currency == null || currency.length() != 3)
            throw  new IllegalArgumentException("Invalid currency code");


        JournalLine line = new JournalLine();
        line.journalEntry = journalEntry;
        line.account = account;
        line.entryType = entryType;
        line.amount = amount;
        line.currency = currency;
        line.createdAt = LocalDateTime.now();

        return line;
    }

    private static BigDecimal normalize(BigDecimal amount) {
        return amount.setScale(4, BigDecimal.ROUND_HALF_UP);
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(!(o instanceof JournalLine)) return false;

        JournalLine that = (JournalLine) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
