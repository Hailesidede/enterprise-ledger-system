package com.example.enterprise_ledger_system.services;

import com.example.enterprise_ledger_system.dtos.CreateJournalLineRequest;
import com.example.enterprise_ledger_system.dtos.CreateJournalRequest;
import com.example.enterprise_ledger_system.entity.Account;
import com.example.enterprise_ledger_system.entity.JournalEntry;
import com.example.enterprise_ledger_system.entity.JournalLine;
import com.example.enterprise_ledger_system.enums.JournalEntryStatus;
import com.example.enterprise_ledger_system.repositories.AccountRepository;
import com.example.enterprise_ledger_system.repositories.JournalEntryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JournalService {

    private final JournalEntryRepository journalEntryRepository;
    private final AccountRepository accountRepository;


    @Transactional
    public UUID createDraftJournal(CreateJournalRequest request){

        JournalEntry journal = new JournalEntry();
        journal.setReference(UUID.randomUUID());
        journal.setStatus(JournalEntryStatus.DRAFT);
        journal.setCreatedAt(LocalDateTime.now());

        for(CreateJournalLineRequest lineRequest : request.lines()){
            Account account = accountRepository.findByPublicId(lineRequest.accountPublicId())
                    .orElseThrow(()-> new IllegalArgumentException("Account not found"));

            JournalLine line = JournalLine.create(
                    journal,
                    account,
                    lineRequest.entryType(),
                    lineRequest.amount(),
                    account.getCurrency()
            );

            journal.addLine(line);
        }

        journalEntryRepository.save(journal);

        return journal.getReference();
    }
}
