package com.pawlik.convertap.entity;

import com.pawlik.convertap.exception.InsufficientFundsException;
import com.pawlik.convertap.exception.InvalidAmountException;
import com.pawlik.convertap.exception.InvalidCurrencyException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AccountTest {

    @Test
    void testHasEnoughMoneyWithSufficientFunds() {
        Account account = new Account();
        account.setBalanceInPln(new BigDecimal("500.00"));
        account.setBalanceInUsd(new BigDecimal("100.00"));

        assertThat(account.hasEnoughMoney("PLN", new BigDecimal("400.00"))).isTrue();
        assertThat(account.hasEnoughMoney("USD", new BigDecimal("100.00"))).isTrue();
    }

    @Test
    void testHasEnoughMoneyWithInsufficientFunds() {
        Account account = new Account();
        account.setBalanceInPln(new BigDecimal("500.00"));
        account.setBalanceInUsd(new BigDecimal("100.00"));

        assertThat(account.hasEnoughMoney("PLN", new BigDecimal("600.00"))).isFalse();
        assertThat(account.hasEnoughMoney("USD", new BigDecimal("200.00"))).isFalse();
    }

    @Test
    void testHasEnoughMoneyWithInvalidCurrency() {
        Account account = new Account();

        assertThatThrownBy(() -> account.hasEnoughMoney("EUR", new BigDecimal("50.00")))
                .isInstanceOf(InvalidCurrencyException.class)
                .hasMessage("Unsupported currency: EUR");
    }

    @Test
    void testDebitWithSufficientFunds() {
        Account account = new Account();
        account.setBalanceInPln(new BigDecimal("500.00"));
        account.setBalanceInUsd(new BigDecimal("100.00"));

        account.debit("PLN", new BigDecimal("100.00"));
        account.debit("USD", new BigDecimal("50.00"));

        assertThat(account.getBalanceInPln()).isEqualByComparingTo(new BigDecimal("400.00"));
        assertThat(account.getBalanceInUsd()).isEqualByComparingTo(new BigDecimal("50.00"));
    }

    @Test
    void testDebitWithInsufficientFunds() {
        Account account = new Account();
        account.setBalanceInPln(new BigDecimal("500.00"));

        assertThatThrownBy(() -> account.debit("PLN", new BigDecimal("600.00")))
                .isInstanceOf(InsufficientFundsException.class)
                .hasMessage("Insufficient funds in PLN");
    }

    @Test
    void testDebitWithInvalidCurrency() {
        Account account = new Account();

        assertThatThrownBy(() -> account.debit("EUR", new BigDecimal("50.00")))
                .isInstanceOf(InvalidCurrencyException.class)
                .hasMessage("Unsupported currency: EUR");
    }

    @Test
    void testCreditWithValidAmount() {
        Account account = new Account();
        account.setBalanceInPln(new BigDecimal("500.00"));
        account.setBalanceInUsd(new BigDecimal("100.00"));

        account.credit("PLN", new BigDecimal("100.00"));
        account.credit("USD", new BigDecimal("50.00"));

        assertThat(account.getBalanceInPln()).isEqualByComparingTo(new BigDecimal("600.00"));
        assertThat(account.getBalanceInUsd()).isEqualByComparingTo(new BigDecimal("150.00"));
    }
}