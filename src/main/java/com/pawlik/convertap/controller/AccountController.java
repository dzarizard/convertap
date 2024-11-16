package com.pawlik.convertap.controller;


import com.pawlik.convertap.dto.CreateAccountRequest;
import com.pawlik.convertap.entity.Account;
import com.pawlik.convertap.repository.AccountRepository;
import com.pawlik.convertap.service.AccountService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;
    @Autowired
    private AccountRepository accountRepository;

    @GetMapping("/get/{identifier}")
    public Account getAccount(@PathVariable String identifier) {
        return accountRepository.findByIdentifier(identifier)
                .orElseThrow(() -> new EntityNotFoundException("Account not found for identifier: " + identifier));
    }

    @PostMapping("/create")
    public ResponseEntity<Account> createAccount(@Valid @RequestBody CreateAccountRequest request) {
        Account createdAccount = accountService.createAccount(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAccount);
    }
}
