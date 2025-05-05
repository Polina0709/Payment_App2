package com.flowbank.service;

import com.flowbank.entity.Card;
import com.flowbank.repository.CardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CardService {

    private final CardRepository cardRepository;

    public List<Card> findByAccountId(UUID accountId) {
        return cardRepository.findByAccountId(accountId);
    }
}
