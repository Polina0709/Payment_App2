package com.flowbank.controller;

import com.flowbank.dto.TopUpRequestDTO;
import com.flowbank.dto.TransferRequestDTO;
import com.flowbank.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransactionControllerTest {

    private final TransactionService transactionService = mock(TransactionService.class);
    private final TransactionController controller = new TransactionController(transactionService);

    @Test
    void transfer_Success() {
        TransferRequestDTO dto = new TransferRequestDTO();
        dto.setSenderAccountNumber("ACC1");
        dto.setReceiverAccountNumber("ACC2");
        dto.setAmount(BigDecimal.valueOf(100));

        when(transactionService.transfer("ACC1", "ACC2", BigDecimal.valueOf(100)))
                .thenReturn(true);

        ResponseEntity<?> response = controller.transfer(dto, "dummy-user-id");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Transfer completed successfully.", response.getBody());
    }

    @Test
    void transfer_Failed() {
        TransferRequestDTO dto = new TransferRequestDTO();
        dto.setSenderAccountNumber("ACC1");
        dto.setReceiverAccountNumber("ACC2");
        dto.setAmount(BigDecimal.valueOf(100));

        when(transactionService.transfer("ACC1", "ACC2", BigDecimal.valueOf(100)))
                .thenReturn(false);

        ResponseEntity<?> response = controller.transfer(dto, "dummy-user-id");

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Transfer failed. Check account status or balance.", response.getBody());
    }

    @Test
    void topUp_Success() {
        TopUpRequestDTO dto = new TopUpRequestDTO();
        dto.setAccountNumber("ACC123");
        dto.setAmount(BigDecimal.valueOf(50));

        when(transactionService.topUp("ACC123", BigDecimal.valueOf(50)))
                .thenReturn(true);

        ResponseEntity<?> response = controller.topUp(dto, "dummy-user-id");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Top-up successful.", response.getBody());
    }

    @Test
    void topUp_Failed() {
        TopUpRequestDTO dto = new TopUpRequestDTO();
        dto.setAccountNumber("ACC123");
        dto.setAmount(BigDecimal.valueOf(50));

        when(transactionService.topUp("ACC123", BigDecimal.valueOf(50)))
                .thenReturn(false);

        ResponseEntity<?> response = controller.topUp(dto, "dummy-user-id");

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Top-up failed. Check if account is blocked.", response.getBody());
    }
}
