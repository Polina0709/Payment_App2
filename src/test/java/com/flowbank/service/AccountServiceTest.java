package com.flowbank.service;

import com.flowbank.entity.Account;
import com.flowbank.repository.AccountRepository;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class AccountServiceTest {

    private final AccountRepository accountRepository = mock(AccountRepository.class);
    LogService logService = mock(LogService.class);
    AccountService accountService = new AccountService(accountRepository, logService);


    @Test
    void testTopUpAccount() {
        Account account = new Account();
        account.setId(UUID.randomUUID());
        account.setBalance(BigDecimal.valueOf(100));

        when(accountRepository.findByAccountNumber("123"))
                .thenReturn(Optional.of(account));

        accountService.topUpAccount("123", BigDecimal.valueOf(50));

        assertEquals(BigDecimal.valueOf(150), account.getBalance());
        verify(accountRepository, times(1)).save(account);
    }
}