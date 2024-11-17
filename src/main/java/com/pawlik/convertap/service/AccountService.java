package com.pawlik.convertap.service;

import com.pawlik.convertap.dto.CreateAccountRequest;
import com.pawlik.convertap.entity.Account;
import com.pawlik.convertap.repository.AccountRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account createAccount(CreateAccountRequest request) {
        Account account = new Account();
        account.setFirstName(request.getFirstName());
        account.setLastName(request.getLastName());
        account.setBalanceInPln(request.getBalance());
        account.setBalanceInUsd(BigDecimal.ZERO);
        account.setIdentifier(UUID.randomUUID().toString());

        return accountRepository.save(account);
    }

    public Account getAccountByIdentifier(String accountIdentifier) {
        return accountRepository.findByIdentifier(accountIdentifier)
                .orElseThrow(() -> new EntityNotFoundException("Account not found for identifier: " + accountIdentifier));
    }

    public void updateAccount(Account account) {
        if (account == null) {
            throw new IllegalArgumentException("Account cannot be null.");
        }

        try {
            accountRepository.save(account);
        } catch (Exception e) {
            throw new RuntimeException("Failed to update account: " + e.getMessage(), e); //TODO change exception
        }
    }

}
