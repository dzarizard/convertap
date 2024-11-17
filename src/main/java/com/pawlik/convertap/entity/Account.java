package com.pawlik.convertap.entity;

import com.pawlik.convertap.exception.InsufficientFundsException;
import com.pawlik.convertap.exception.InvalidAmountException;
import com.pawlik.convertap.exception.InvalidCurrencyException;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
public class Account {

    public Account() {
    }

    public Account(String firstName, String lastName, String identifier, BigDecimal balanceInPln, BigDecimal balanceInUsd) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.identifier = identifier;
        this.balanceInPln = balanceInPln;
        this.balanceInUsd = balanceInUsd;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private String identifier;
    private BigDecimal balanceInPln = BigDecimal.ZERO;
    private BigDecimal balanceInUsd = BigDecimal.ZERO;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public BigDecimal getBalanceInPln() {
        return balanceInPln;
    }

    public void setBalanceInPln(BigDecimal balanceInPln) {
        this.balanceInPln = balanceInPln;
    }

    public BigDecimal getBalanceInUsd() {
        return balanceInUsd;
    }

    public void setBalanceInUsd(BigDecimal balanceInUsd) {
        this.balanceInUsd = balanceInUsd;
    }

    public void debit(String currency, BigDecimal amount) {
        if(!hasEnoughMoney(currency, amount)){
            throw new InsufficientFundsException("Insufficient funds in " + currency);
        }

        switch (currency.toUpperCase()) {
            case "PLN" -> balanceInPln = balanceInPln.subtract(amount);
            case "USD" -> balanceInUsd = balanceInUsd.subtract(amount);
            default -> throw new InvalidCurrencyException("Unsupported currency: " + currency);
        }
    }

    public boolean hasEnoughMoney(String fromCurrency, BigDecimal amount) {
        validateCurrencyAndAmount(fromCurrency, amount);

        return switch (fromCurrency.toUpperCase()) {
            case "USD" -> balanceInUsd.compareTo(amount) >= 0;
            case "PLN" -> balanceInPln.compareTo(amount) >= 0;
            default -> throw new InvalidCurrencyException("Unsupported currency: " + fromCurrency);
        };
    }

    public void credit(String currency, BigDecimal amount) {
        validateCurrencyAndAmount(currency, amount);

        switch (currency.toUpperCase()) {
            case "PLN" -> balanceInPln = balanceInPln.add(amount);
            case "USD" -> balanceInUsd = balanceInUsd.add(amount);
            default -> throw new InvalidCurrencyException("Unsupported currency: " + currency);
        }
    }

     static void validateCurrencyAndAmount(String currency, BigDecimal amount) {
        if (currency == null || currency.trim().isEmpty()) {
            throw new InvalidCurrencyException("Currency cannot be null or empty.");
        }
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException("Amount must be greater than zero.");
        }
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Objects.equals(id, account.id) && Objects.equals(identifier, account.identifier);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, identifier);
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", identifier='" + identifier + '\'' +
                ", balanceInPln=" + balanceInPln +
                ", balanceInUsd=" + balanceInUsd +
                '}';
    }
}