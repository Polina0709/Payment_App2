package com.flowbank.service;

import com.flowbank.entity.Account;
import com.flowbank.repository.AccountRepository;
import com.flowbank.repository.TransactionRepository;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransactionServiceTest {

    private final AccountRepository accountRepository = mock(AccountRepository.class);
    private final TransactionRepository transactionRepository = mock(TransactionRepository.class);
    LogService logService = mock(LogService.class);
    private final TransactionService transactionService = new TransactionService(accountRepository, transactionRepository, logService);

    @Test
    void testSuccessfulTransfer() {
        Account sender = new Account();
        sender.setBalance(BigDecimal.valueOf(100));
        sender.setBlocked(false);

        Account receiver = new Account();
        receiver.setBalance(BigDecimal.valueOf(20));
        receiver.setBlocked(false);

        when(accountRepository.findByAccountNumber("SENDER")).thenReturn(Optional.of(sender));
        when(accountRepository.findByAccountNumber("RECEIVER")).thenReturn(Optional.of(receiver));

        boolean result = transactionService.transfer("SENDER", "RECEIVER", BigDecimal.valueOf(30));

        assertTrue(result);
        assertEquals(BigDecimal.valueOf(70), sender.getBalance());
        assertEquals(BigDecimal.valueOf(50), receiver.getBalance());
        verify(accountRepository, times(1)).save(sender);
        verify(accountRepository, times(1)).save(receiver);
        verify(transactionRepository, times(1)).save(any());
    }
}