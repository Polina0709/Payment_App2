package com.flowbank.controller;

import com.flowbank.service.AccountService;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AccountControllerTest {

    private final AccountService accountService = mock(AccountService.class);
    private final AccountController controller = new AccountController(accountService);

    @Test
    void clientBlocksOwnAccount_Success() {
        UUID accountId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        when(accountService.blockAccountIfOwner(accountId, userId)).thenReturn(true);

        ResponseEntity<?> response = controller.blockOwnAccount(accountId, userId.toString());

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Account successfully blocked.", response.getBody());
    }

    @Test
    void clientBlocksNotOwnAccount_Forbidden() {
        UUID accountId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        when(accountService.blockAccountIfOwner(accountId, userId)).thenReturn(false);

        ResponseEntity<?> response = controller.blockOwnAccount(accountId, userId.toString());

        assertEquals(403, response.getStatusCodeValue());
        assertEquals("You can only block your own account.", response.getBody());
    }

    @Test
    void adminUnblocksAccount_Success() {
        UUID accountId = UUID.randomUUID();

        doNothing().when(accountService).unblockAccount(accountId);

        ResponseEntity<?> response = controller.unblockAccount(accountId);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Account successfully unblocked by admin.", response.getBody());
        verify(accountService, times(1)).unblockAccount(accountId);
    }
}
