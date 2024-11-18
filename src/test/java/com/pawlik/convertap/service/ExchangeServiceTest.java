package com.pawlik.convertap.service;

import com.pawlik.convertap.entity.Account;
import com.pawlik.convertap.exception.ExchangeValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

class ExchangeServiceTest {

    @Mock
    private AccountService accountService;

    @Mock
    private RateService rateService;

    @InjectMocks
    private ExchangeService exchangeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testExchangePlnToUsdSuccess() {
        String accountId = "test-account";
        Account mockAccount = new Account();
        mockAccount.setBalanceInPln(new BigDecimal("1000.00"));
        mockAccount.setBalanceInUsd(BigDecimal.ZERO);

        BigDecimal amount = new BigDecimal("500.00");
        BigDecimal exchangeRate = new BigDecimal("4.50");

        when(accountService.getAccountByIdentifier(accountId)).thenReturn(mockAccount);
        when(rateService.getExchangeUSDRate()).thenReturn(exchangeRate);

        BigDecimal result = exchangeService.exchange(accountId, "PLN", "USD", amount);

        assertThat(result).isEqualByComparingTo(new BigDecimal("111.11"));
    }

    @Test
    void testExchangeUsdToPlnSuccess() {
        String accountId = "test-account";
        Account mockAccount = new Account();
        mockAccount.setBalanceInPln(BigDecimal.ZERO);
        mockAccount.setBalanceInUsd(new BigDecimal("200.00"));

        BigDecimal amount = new BigDecimal("100.00");
        BigDecimal exchangeRate = new BigDecimal("4.50");

        when(accountService.getAccountByIdentifier(accountId)).thenReturn(mockAccount);
        when(rateService.getExchangeUSDRate()).thenReturn(exchangeRate);

        BigDecimal result = exchangeService.exchange(accountId, "USD", "PLN", amount);

        assertThat(result).isEqualByComparingTo(new BigDecimal("450.00"));
    }

    @Test
    void testExchangeWithUnsupportedCurrency() {
        String accountId = "test-account";
        BigDecimal amount = new BigDecimal("100.00");

        assertThatThrownBy(() -> exchangeService.exchange(accountId, "EUR", "PLN", amount))
                .isInstanceOf(ExchangeValidationException.class)
                .hasMessageContaining("Unsupported currency conversion");
    }

    @Test
    void testExchangeWithNullCurrency() {
        String accountId = "test-account";
        BigDecimal amount = new BigDecimal("100.00");

        assertThatThrownBy(() -> exchangeService.exchange(accountId, null, "PLN", amount))
                .isInstanceOf(ExchangeValidationException.class)
                .hasMessageContaining("Currency values cannot be null");
    }

    @Test
    void testExchangeWithNegativeAmount() {
        String accountId = "test-account";
        BigDecimal amount = new BigDecimal("-100.00");

        assertThatThrownBy(() -> exchangeService.exchange(accountId, "PLN", "USD", amount))
                .isInstanceOf(ExchangeValidationException.class)
                .hasMessageContaining("Amount must be greater than zero");
    }
}