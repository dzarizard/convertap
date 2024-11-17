package com.pawlik.convertap.service;

import com.pawlik.convertap.dto.CreateAccountRequest;
import com.pawlik.convertap.entity.Account;
import com.pawlik.convertap.exception.AccountUpdateException;
import com.pawlik.convertap.exception.InvalidAmountException;
import com.pawlik.convertap.repository.AccountRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private static final BigDecimal MAX_NUMERIC_VALUE = new BigDecimal("9999999999.99");

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account createAccount(CreateAccountRequest request) {
        validateAccountBalance(request.getBalance());

        Account account = new Account();
        account.setFirstName(request.getFirstName());
        account.setLastName(request.getLastName());
        account.setBalanceInPln(request.getBalance  ());
        account.setBalanceInUsd(BigDecimal.ZERO);
        account.setIdentifier(UUID.randomUUID().toString());

        return accountRepository.save(account);
    }

    public Account getAccountByIdentifier(String accountIdentifier) {
        return accountRepository.findByIdentifier(accountIdentifier)
                .orElseThrow(() -> new EntityNotFoundException("Account not found for identifier: " + accountIdentifier));
    }

    public void updateAccount(Account account) {
     try {
            accountRepository.save(account);
        } catch (Exception e) {
            throw new AccountUpdateException("Failed to update account: " + account.getIdentifier(), e);
        }
    }

    public void validateAccountBalance(BigDecimal balance) {
        if (balance != null && balance.compareTo(MAX_NUMERIC_VALUE) > 0) {
            throw new InvalidAmountException("Balance exceeds the maximum value: " + MAX_NUMERIC_VALUE);
        }
    }
}
