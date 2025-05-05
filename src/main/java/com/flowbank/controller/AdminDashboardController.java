package com.flowbank.controller;

import com.flowbank.dto.AdminAccountResponseDTO;
import com.flowbank.dto.CardResponseDTO;
import com.flowbank.entity.Account;
import com.flowbank.entity.Card;
import com.flowbank.service.AccountService;
import com.flowbank.service.CardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminDashboardController {

    private final AccountService accountService;
    private final CardService cardService;

    // ✅ Отримати всі рахунки
    @GetMapping("/accounts")
    public ResponseEntity<List<AdminAccountResponseDTO>> getAllAccounts() {
        List<Account> accounts = accountService.findAllAccounts();

        List<AdminAccountResponseDTO> result = accounts.stream().map(account -> {
            AdminAccountResponseDTO dto = new AdminAccountResponseDTO();
            dto.setId(account.getId()); // 🔥 ОБОВ’ЯЗКОВО
            dto.setAccountNumber(account.getAccountNumber());
            dto.setBlocked(account.isBlocked());
            dto.setBalance(account.getBalance());
            dto.setOwnerFullName(account.getUser().getFullName());

            List<CardResponseDTO> cards = cardService.findByAccountId(account.getId())
                    .stream()
                    .map(this::mapCardToDto)
                    .collect(Collectors.toList());

            dto.setCards(cards);
            return dto;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }

    private CardResponseDTO mapCardToDto(Card card) {
        CardResponseDTO dto = new CardResponseDTO();
        dto.setCardNumber(card.getCardNumber());
        dto.setExpirationDate(LocalDate.parse(card.getExpirationDate().toString()));
        return dto;
    }
}

