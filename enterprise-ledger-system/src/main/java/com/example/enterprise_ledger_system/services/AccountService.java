package com.example.enterprise_ledger_system.services;

import com.example.enterprise_ledger_system.dtos.AccountDto;
import com.example.enterprise_ledger_system.dtos.AccountsResponse;
import com.example.enterprise_ledger_system.entity.Account;
import com.example.enterprise_ledger_system.repositories.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    @Transactional(readOnly = true)
    public AccountsResponse getAccounts(){
        List<Account> accounts = accountRepository.findAll();

        List<AccountDto> accountDtos = accounts.stream()
                .map(this::toDtos)
                .toList();

        return  AccountsResponse.builder()
                .accounts(accountDtos)
                .totalRecords(accountDtos.size())
                .responseTimeStamp(LocalDateTime.now())
                .responseCode("00")
                .responseMessage("Accounts retrieved successfully")
                .build();

    }

    private AccountDto toDtos(Account account) {
        return AccountDto.builder()
                .publicId(account.getPublicId())
                .accountNumber(account.getAccountNumber())
                .accountName(account.getAccountName())
                .accountCode(account.getAccountCode())
                .accountType(account.getAccountType())
                .normalBalance(account.getNormalBalance())
                .parentAccountPublicId(
                        account.getParentAccount() != null
                        ? account.getParentAccount().getPublicId()
                                :null
                )
                .levelDepth(account.getLevelDepth())
                .balance(account.getBalance())
                .currency(account.getCurrency())
                .allowNegativeBalance(account.isAllowNegativeBalance())
                .reconcilable(account.isReconcilable())
                .locked(account.isLocked())
                .systemAccount(account.isSystemAccount())
                .status(account.isStatus())
                .createdAt(account.getCreatedAt())
                .updatedAt(account.getUpdatedAt())
                .build();
    }
}
