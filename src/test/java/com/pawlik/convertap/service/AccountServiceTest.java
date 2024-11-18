package com.pawlik.convertap.service;

import com.pawlik.convertap.dto.CreateAccountRequest;
import com.pawlik.convertap.entity.Account;
import com.pawlik.convertap.exception.AccountUpdateException;
import com.pawlik.convertap.exception.InvalidAmountException;
import com.pawlik.convertap.repository.AccountRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountService accountService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateAccountWithValidData() {
        CreateAccountRequest request = new CreateAccountRequest();
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setBalance(new BigDecimal("500.00"));

        Account mockAccount = new Account("John", "Doe", "random-uuid", new BigDecimal("500.00"), BigDecimal.ZERO);
        when(accountRepository.save(any(Account.class))).thenReturn(mockAccount);

        Account result = accountService.createAccount(request);

        assertThat(result.getFirstName()).isEqualTo("John");
        assertThat(result.getLastName()).isEqualTo("Doe");
        assertThat(result.getBalanceInPln()).isEqualTo(new BigDecimal("500.00"));
        assertThat(result.getBalanceInUsd()).isEqualTo(BigDecimal.ZERO);
    }

    @Test
    void testCreateAccountWithExceedingBalance() {
        CreateAccountRequest request = new CreateAccountRequest();
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setBalance(new BigDecimal("10000000000.00"));

        assertThatThrownBy(() -> accountService.createAccount(request))
            .isInstanceOf(InvalidAmountException.class)
            .hasMessageContaining("Balance exceeds the maximum value");
    }

    @Test
    void testGetAccountByIdentifierFound() {
        Account mockAccount = new Account("Jane", "Doe", "test-identifier", new BigDecimal("1000.00"), BigDecimal.ZERO);
        when(accountRepository.findByIdentifier("test-identifier")).thenReturn(Optional.of(mockAccount));

        Account result = accountService.getAccountByIdentifier("test-identifier");

        assertThat(result.getFirstName()).isEqualTo("Jane");
        assertThat(result.getLastName()).isEqualTo("Doe");
        assertThat(result.getBalanceInPln()).isEqualByComparingTo(new BigDecimal("1000.00"));
    }

    @Test
    void testGetAccountByIdentifierNotFound() {
        when(accountRepository.findByIdentifier("invalid-identifier")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> accountService.getAccountByIdentifier("invalid-identifier"))
            .isInstanceOf(EntityNotFoundException.class)
            .hasMessageContaining("Account not found for identifier: invalid-identifier");
    }

    @Test
    void testUpdateAccountSuccessful() {
        Account mockAccount = new Account("Jane", "Doe", "test-identifier", new BigDecimal("500.00"), BigDecimal.ZERO);
        when(accountRepository.save(mockAccount)).thenReturn(mockAccount);

        accountService.updateAccount(mockAccount);

        verify(accountRepository, times(1)).save(mockAccount);
    }

    @Test
    void testUpdateAccountFailure() {
        Account mockAccount = new Account("Jane", "Doe", "test-identifier", new BigDecimal("500.00"), BigDecimal.ZERO);
        doThrow(new RuntimeException("Database error")).when(accountRepository).save(mockAccount);

        assertThatThrownBy(() -> accountService.updateAccount(mockAccount))
            .isInstanceOf(AccountUpdateException.class)
            .hasMessageContaining("Failed to update account: test-identifier");
    }
}