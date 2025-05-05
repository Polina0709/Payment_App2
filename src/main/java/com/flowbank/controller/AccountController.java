package com.flowbank.controller;

import com.flowbank.dto.AccountResponseDTO;
import com.flowbank.entity.Account;
import com.flowbank.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/client/accounts")
    public ResponseEntity<List<AccountResponseDTO>> getClientAccounts(@AuthenticationPrincipal String userId) {
        List<Account> accounts = accountService.findByUserId(UUID.fromString(userId));
        List<AccountResponseDTO> response = accounts.stream().map(this::mapToDto).toList();

        log.info("AccountController - Client {} requested their accounts", userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/admin/accounts")
    public ResponseEntity<List<AccountResponseDTO>> getAllAccounts() {
        List<Account> accounts = accountService.findAllAccounts();
        List<AccountResponseDTO> response = accounts.stream().map(this::mapToDto).toList();

        log.info("AccountController - Admin requested all client accounts");
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/block/{id}")
    public ResponseEntity<?> blockOwnAccount(@PathVariable UUID id,
                                             @AuthenticationPrincipal String userId) {
        boolean success = accountService.blockAccountIfOwner(id, UUID.fromString(userId));
        if (!success) {
            log.warn("AccountController - Client {} tried to block someone else's account {}", userId, id);
            return ResponseEntity.status(403).body("You can only block your own account.");
        }

        log.info("AccountController - Client {} blocked account {}", userId, id);
        return ResponseEntity.ok("Account successfully blocked.");
    }

    @PatchMapping("/admin/block/{id}")
    public ResponseEntity<?> blockAccountByAdmin(@PathVariable UUID id) {
        accountService.blockAccount(id);
        return ResponseEntity.ok("Account successfully blocked by admin.");
    }

    @PatchMapping("/unblock/{id}")
    public ResponseEntity<?> unblockAccount(@PathVariable UUID id) {
        accountService.unblockAccount(id);
        log.info("AccountController - Admin unblocked account {}", id);
        return ResponseEntity.ok("Account successfully unblocked by admin.");
    }

    private AccountResponseDTO mapToDto(Account acc) {
        AccountResponseDTO dto = new AccountResponseDTO();
        dto.setId(acc.getId());
        dto.setAccountNumber(acc.getAccountNumber());
        dto.setBalance(acc.getBalance());
        dto.setBlocked(acc.isBlocked());
        return dto;
    }
}