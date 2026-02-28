package com.example.enterprise_ledger_system.dtos;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.List;

@Value
@Builder
public class AccountsResponse {
    List<AccountDto> accounts;

    long totalRecords;
    LocalDateTime responseTimeStamp;

    String responseCode;
    String responseMessage;
}
