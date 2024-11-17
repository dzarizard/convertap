package com.pawlik.convertap.service;

import com.pawlik.convertap.entity.Account;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class ExchangeService {

    private final AccountService accountService;
    private final RateService rateService;

    public ExchangeService(AccountService accountService, RateService rateService) {
        this.accountService = accountService;
        this.rateService = rateService;
    }

    //TODO refactor, duplicates, exceptions, validation, short methods
    public BigDecimal exchange(String accountIdentifier, String fromCurrency, String toCurrency, BigDecimal amount) {
        Account account = accountService.getAccountByIdentifier(accountIdentifier);
        BigDecimal convertedAmount;

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero.");
        }

        if ("PLN".equalsIgnoreCase(fromCurrency) && "USD".equalsIgnoreCase(toCurrency)) {
            BigDecimal exchangeUSDRate = rateService.getExchangeUSDRate();
            convertedAmount = amount.divide(exchangeUSDRate,2, RoundingMode.HALF_UP);

            if (account.hasEnoughMoney(fromCurrency, amount)) {
                account.debit(fromCurrency, amount);
                account.credit(toCurrency, convertedAmount);
            } else {
                throw new RuntimeException("Insufficient funds in " + fromCurrency);
            }

        } else if ("USD".equalsIgnoreCase(fromCurrency) && "PLN".equalsIgnoreCase(toCurrency)) {
            BigDecimal exchangeRate = rateService.getExchangeUSDRate();
            convertedAmount = amount.multiply(exchangeRate).setScale(2, RoundingMode.HALF_EVEN);

            if (account.hasEnoughMoney(fromCurrency, amount)) {
                account.debit(fromCurrency, amount);
                account.credit(toCurrency, convertedAmount);
            } else {
                throw new RuntimeException("Insufficient funds in " + fromCurrency);
            }

        } else {
            throw new RuntimeException("Currency conversion from " + fromCurrency + " to " + toCurrency + " is not supported.");
        }

        accountService.updateAccount(account);
        return convertedAmount;
    }
}
