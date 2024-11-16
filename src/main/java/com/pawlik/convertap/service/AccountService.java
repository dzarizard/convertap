package com.pawlik.convertap.service;

import com.pawlik.convertap.dto.CreateAccountRequest;
import com.pawlik.convertap.entity.Account;
import com.pawlik.convertap.repository.AccountRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    public Account createAccount(CreateAccountRequest request) {
        Account account = new Account();
        account.setFirstName(request.getFirstName());
        account.setLastName(request.getLastName());
        account.setBalanceInPln(request.getBalance());
        account.setBalanceInUsd(BigDecimal.ZERO);
        account.setIdentifier(UUID.randomUUID().toString()); //TODO TBU OR CONFIRM

        return accountRepository.save(account);
    }

    public Account getAccountByIdentifier(String accountIdentifier) {
        return accountRepository.findByIdentifier(accountIdentifier)
                .orElseThrow(() -> new EntityNotFoundException("Account not found for identifier: " + accountIdentifier));    }

}
