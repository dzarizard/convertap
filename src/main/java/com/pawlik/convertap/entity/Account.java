package com.pawlik.convertap.entity;

import jakarta.persistence.*;

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
}