package com.flowbank.controller;

import com.flowbank.dto.AccountResponseDTO;
import com.flowbank.dto.CardResponseDTO;
import com.flowbank.entity.Account;
import com.flowbank.entity.Card;
import com.flowbank.service.AccountService;
import com.flowbank.service.CardService;
import com.flowbank.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/client")
@RequiredArgsConstructor
public class ClientDashboardController {

    private final UserService userService;
    private final AccountService accountService;
    private final CardService cardService;

    @GetMapping("/accounts")
    public ResponseEntity<?> getAccounts(@AuthenticationPrincipal String userId) {
        UUID uid = UUID.fromString(userId);
        List<Account> accounts = accountService.findByUserId(uid);

        List<AccountResponseDTO> response = accounts.stream().map(account -> {
            AccountResponseDTO dto = new AccountResponseDTO();
            dto.setAccountNumber(account.getAccountNumber());
            dto.setBlocked(account.isBlocked());
            dto.setBalance(account.getBalance());

            List<CardResponseDTO> cards = cardService.findByAccountId(account.getId())
                    .stream()
                    .map(this::mapCardToDto)
                    .collect(Collectors.toList());

            dto.setCards(cards);
            return dto;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    private CardResponseDTO mapCardToDto(Card card) {
        CardResponseDTO dto = new CardResponseDTO();
        dto.setCardNumber(card.getCardNumber());
        dto.setExpirationDate(LocalDate.parse(card.getExpirationDate().toString()));
        return dto;
    }
}
