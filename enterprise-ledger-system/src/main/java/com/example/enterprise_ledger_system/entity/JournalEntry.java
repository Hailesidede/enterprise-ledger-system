package com.example.enterprise_ledger_system.entity;

import com.example.enterprise_ledger_system.enums.JournalEntryStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(
        name = "JOURNAL_ENTRIES",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_journal_refrence", columnNames = "reference"),
                @UniqueConstraint( name = "uk_journal_idempotency", columnNames = "idempotency_key")
        }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor( access = AccessLevel.PROTECTED)
public class JournalEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, updatable = false)
    public UUID reference;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private JournalEntryStatus status;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime postedAt;

    @Column(name = "idempotency_key", length = 100, unique = true, updatable = false)
    private String idempotencyKey;

    @Column(length = 100, updatable = false)
    private String createdBy;

    @Column(length = 100)
    private String postedBy;

    @Column(length = 255)
    private String description;

    @Version
    private Long version;


    @OneToMany(mappedBy = "journalEntry",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<JournalLine> lines = new ArrayList<>();



    private static JournalEntry createDraft(String createdBy, String description){
        JournalEntry journalEntry = new JournalEntry();
        journalEntry.reference = UUID.randomUUID();
        journalEntry.status = JournalEntryStatus.DRAFT;
        journalEntry.createdAt = LocalDateTime.now();
        journalEntry.createdBy = createdBy;
        journalEntry.description = description;

        return journalEntry;
    }

    public void addLine(JournalLine line){
        if(this.status != JournalEntryStatus.DRAFT){
            throw new IllegalStateException("Cannot modify non-draft journal");
        }

        line.setJournalEntry(this);
        this.lines.add(line);
    }

    public void markPosted(String postedBy, String idempotencyKey){
        if(this.status != JournalEntryStatus.DRAFT){
            throw new IllegalStateException("Journal already processed");
        }

        this.status = JournalEntryStatus.POSTED;
        this.postedAt = LocalDateTime.now();
        this.postedBy = postedBy;
        this.idempotencyKey = idempotencyKey;
    }

    public boolean isDraft(){
        return this.status == JournalEntryStatus.DRAFT;
    }

    public boolean isPosted(){
        return this.status == JournalEntryStatus.POSTED;
    }
}
