package com.example.enterprise_ledger_system.controller;

import com.example.enterprise_ledger_system.dtos.AccountsResponse;
import com.example.enterprise_ledger_system.services.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
@Validated
public class AccountsController {
    private final AccountService accountService;

    @GetMapping
    public ResponseEntity<AccountsResponse> getAccounts() {
        return ResponseEntity.ok(accountService.getAccounts());
    }
}
