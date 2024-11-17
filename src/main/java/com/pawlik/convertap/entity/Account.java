package com.pawlik.convertap.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.math.BigDecimal;

@Entity
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private String identifier;
    private BigDecimal balanceInPln;
    private BigDecimal balanceInUsd;

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


    //TODO refactor methods
    public boolean hasEnoughMoney(String fromCurrency, BigDecimal amount) {
        if ("USD".equalsIgnoreCase(fromCurrency) && balanceInUsd.compareTo(amount) >= 0) {
            return true;
        }
        return "PLN".equalsIgnoreCase(fromCurrency) && balanceInPln.compareTo(amount) >= 0;
    }

    public void debit(String currency, BigDecimal amount) {
        if (currency == null || amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Invalid currency or amount for debit operation.");
        }

        switch (currency.toUpperCase()) {
            case "PLN":
                if (this.getBalanceInPln().compareTo(amount) < 0) {
                    throw new RuntimeException("Insufficient funds in PLN.");
                }
                this.setBalanceInPln(this.getBalanceInPln().subtract(amount));
                break;

            case "USD":
                if (this.getBalanceInUsd().compareTo(amount) < 0) {
                    throw new RuntimeException("Insufficient funds in USD.");
                }
                this.setBalanceInUsd(this.getBalanceInUsd().subtract(amount));
                break;

            default:
                throw new RuntimeException("Unsupported currency: " + currency);
        }
    }

    public void credit(String currency, BigDecimal amount) {
        if (currency == null || amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Invalid currency or amount for credit operation.");
        }

        switch (currency.toUpperCase()) {
            case "PLN":
                this.setBalanceInPln(this.getBalanceInPln().add(amount));
                break;

            case "USD":
                this.setBalanceInUsd(this.getBalanceInUsd().add(amount));
                break;

            default:
                throw new RuntimeException("Unsupported currency: " + currency);
        }
    }
}