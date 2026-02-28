package com.example.enterprise_ledger_system.services;

import com.example.enterprise_ledger_system.entity.Account;
import com.example.enterprise_ledger_system.entity.JournalEntry;
import com.example.enterprise_ledger_system.entity.JournalLine;
import com.example.enterprise_ledger_system.enums.AccountType;
import com.example.enterprise_ledger_system.enums.EntryType;
import com.example.enterprise_ledger_system.enums.JournalEntryStatus;
import com.example.enterprise_ledger_system.repositories.AccountRepository;
import com.example.enterprise_ledger_system.repositories.JournalEntryRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.example.enterprise_ledger_system.enums.AccountType.*;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final AccountRepository accountRepository;
    private final JournalEntryRepository journalEntryRepository;

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void postJournalEntry(UUID journalReference, String idempotencykey) {
        JournalEntry journal = journalEntryRepository
                .findByReferenceForUpdate(journalReference)
                .orElseThrow(()-> new IllegalArgumentException("Journal not found"));

        if(journal.getIdempotencyKey() != null &&
                journal.getIdempotencyKey().equals(idempotencykey)){
            return;
        }

        if(journal.getStatus() != JournalEntryStatus.DRAFT){
            throw new IllegalArgumentException("Journal already processed");
        }

        if(journal.getLines().size() < 2){
            throw new IllegalArgumentException("Minimum two legs required");
        }

        BigDecimal totalDebits = BigDecimal.ZERO;
        BigDecimal totalCredits = BigDecimal.ZERO;

        for(JournalLine line : journal.getLines()){
            if(line.getAmount().compareTo(BigDecimal.ZERO) <= 0)
                throw new IllegalArgumentException("Invalid line amount");

            if(line.getEntryType() == EntryType.DEBIT)
                totalDebits = totalDebits.add(line.getAmount());
            else
                totalCredits = totalCredits.add(line.getAmount());
        }

        if(totalDebits.compareTo(totalCredits) !=0) {
            throw new IllegalArgumentException("Ledger invariat violated: Debits != Credits");
        }

        List<Long> accountIds = journal.getLines().stream()
                .map(line -> line.getAccount().getId())
                .distinct()
                .sorted()
                .toList();

        Map<Long, Account> lockedAccounts = new HashMap<>();

        for(Long accountId :accountIds) {
            Account account = accountRepository
                    .findByIdForUpdate(accountId)
                    .orElseThrow(() -> new IllegalArgumentException("Account is missing"));

            if(!account.isStatus())
                throw new IllegalStateException("Account is not active");

            if(account.isLocked()){
                throw new IllegalArgumentException("Account is locked");
            }

            lockedAccounts.put(accountId, account);
        }

        for(JournalLine line : journal.getLines()) {
            Account account = lockedAccounts.get(line.getAccount().getId());

            BigDecimal newBalance = calculateNewBalance(
                    account,
                    line.getAmount(),
                    line.getEntryType()
            );

            if(!account.isAllowNegativeBalance() &&
                    newBalance.compareTo(BigDecimal.ZERO) < 0) {
                throw new IllegalArgumentException("Insufficient funds :"+ account.getAccountType());
            }

            account.setBalance(newBalance);
        }

        journal.markPosted("SYSTEM", idempotencykey);
        journalEntryRepository.save(journal);
    }

    private BigDecimal calculateNewBalance(Account account,
                                            BigDecimal amount,
                                            EntryType entryType) {
        BigDecimal balance = account.getBalance();

        switch (account.getAccountType()){
            case ASSET:
            case EXPENSE:
                return entryType == EntryType.DEBIT
                        ? balance.add(amount)
                        : balance.subtract(amount);


            case LIABILITY:
            case EQUITY:
            case REVENUE:
                return entryType  == EntryType.CREDIT
                        ? balance.add(amount)
                        : balance.subtract(amount);

            default:
                throw new IllegalArgumentException("Account type not supported");
        }
    }

}
