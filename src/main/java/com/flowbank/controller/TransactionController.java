package com.flowbank.controller;

import com.flowbank.dto.TopUpRequestDTO;
import com.flowbank.dto.TransactionResponseDTO;
import com.flowbank.dto.TransferRequestDTO;
import com.flowbank.entity.Transaction;
import com.flowbank.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/client")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/transfer")
    public ResponseEntity<?> transfer(@RequestBody TransferRequestDTO request,
                                      @AuthenticationPrincipal String userId) {

        boolean success = transactionService.transfer(
                request.getSenderAccountNumber(),
                request.getReceiverAccountNumber(),
                request.getAmount());

        if (!success) {
            return ResponseEntity.badRequest().body("Transfer failed. Check account status or balance.");
        }

        log.info("TransactionController - Client {} transferred {} from {} to {}",
                userId, request.getAmount(), request.getSenderAccountNumber(), request.getReceiverAccountNumber());

        return ResponseEntity.ok("Transfer completed successfully.");
    }

    @PostMapping("/topup")
    public ResponseEntity<?> topUp(@RequestBody TopUpRequestDTO request,
                                   @AuthenticationPrincipal String userId) {

        boolean success = transactionService.topUp(request.getAccountNumber(), request.getAmount());

        if (!success) {
            return ResponseEntity.badRequest().body("Top-up failed. Check if account is blocked.");
        }

        log.info("TransactionController - Client {} topped up account {} by {}",
                userId, request.getAccountNumber(), request.getAmount());

        return ResponseEntity.ok("Top-up successful.");
    }

    @GetMapping("/transactions")
    public ResponseEntity<List<TransactionResponseDTO>> getClientTransactions(
            @AuthenticationPrincipal String userId) {

        UUID clientId = UUID.fromString(userId);
        List<Transaction> transactions = transactionService.getByClientId(clientId);

        List<TransactionResponseDTO> response = transactions.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    private TransactionResponseDTO mapToDto(Transaction tx) {
        TransactionResponseDTO dto = new TransactionResponseDTO();
        dto.setId(tx.getId());
        dto.setSenderAccountId(tx.getSenderAccount().getId());
        dto.setReceiverAccountId(tx.getReceiverAccount().getId());
        dto.setAmount(tx.getAmount());
        dto.setCreatedAt(tx.getCreatedAt());
        return dto;
    }
}
