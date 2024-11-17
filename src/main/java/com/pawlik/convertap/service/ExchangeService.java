package com.pawlik.convertap.service;

import com.pawlik.convertap.entity.Account;
import com.pawlik.convertap.exception.ExchangeValidationException;
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

    public BigDecimal exchange(String accountIdentifier, String fromCurrency, String toCurrency, BigDecimal amount) {
        validateInputs(fromCurrency, toCurrency, amount);
        Account account = accountService.getAccountByIdentifier(accountIdentifier);

        BigDecimal exchangeUSDRate = rateService.getExchangeUSDRate();
        BigDecimal convertedAmount = calculateConvertedAmount(amount, exchangeUSDRate, fromCurrency, toCurrency);

        proceedExchange(fromCurrency, toCurrency, amount, account, convertedAmount);
        accountService.updateAccount(account);
        return convertedAmount;
    }

    private void validateInputs(String fromCurrency, String toCurrency, BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ExchangeValidationException("Amount must be greater than zero.");
        }

        if (fromCurrency == null || toCurrency == null) {
            throw new ExchangeValidationException("Currency values cannot be null.");
        }

        if (!("PLN".equalsIgnoreCase(fromCurrency) && "USD".equalsIgnoreCase(toCurrency)) &&
                !("USD".equalsIgnoreCase(fromCurrency) && "PLN".equalsIgnoreCase(toCurrency))) {
            throw new ExchangeValidationException("Unsupported currency conversion: " + fromCurrency + " to " + toCurrency);
        }
    }

    private BigDecimal calculateConvertedAmount(BigDecimal amount, BigDecimal exchangeUSDRate, String fromCurrency, String toCurrency) {
        if ("PLN".equalsIgnoreCase(fromCurrency) && "USD".equalsIgnoreCase(toCurrency)) {
            return amount.divide(exchangeUSDRate, 2, RoundingMode.HALF_EVEN);
        } else if ("USD".equalsIgnoreCase(fromCurrency) && "PLN".equalsIgnoreCase(toCurrency)) {
            return amount.multiply(exchangeUSDRate).setScale(2, RoundingMode.HALF_EVEN);
        } else {
            throw new ExchangeValidationException("Unsupported conversion logic for: " + fromCurrency + " to " + toCurrency);
        }
    }

    private void proceedExchange(String fromCurrency, String toCurrency, BigDecimal amount, Account account, BigDecimal convertedAmount) {
        account.debit(fromCurrency, amount);
        account.credit(toCurrency, convertedAmount);
    }
}
