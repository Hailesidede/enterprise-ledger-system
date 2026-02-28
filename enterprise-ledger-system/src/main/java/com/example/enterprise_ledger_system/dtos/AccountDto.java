package com.example.enterprise_ledger_system.dtos;

import com.example.enterprise_ledger_system.enums.AccountType;
import com.example.enterprise_ledger_system.enums.NormalBalance;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Value
@Builder
public class AccountDto {
    UUID publicId;
    String accountNumber;
    String accountName;
    String accountCode;

    AccountType accountType;
    NormalBalance normalBalance;

    UUID parentAccountPublicId;
    Integer levelDepth;

    BigDecimal balance;
    String currency;

    boolean allowNegativeBalance;
    boolean reconcilable;
    boolean locked;
    boolean systemAccount;
    boolean status;

    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
