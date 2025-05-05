package com.flowbank.service;

import com.flowbank.entity.Card;
import com.flowbank.repository.CardRepository;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CardServiceTest {

    private final CardRepository cardRepository = mock(CardRepository.class);
    private final CardService cardService = new CardService(cardRepository);

    @Test
    void testFindByAccountId() {
        UUID accountId = UUID.randomUUID();
        List<Card> cards = Arrays.asList(new Card(), new Card());

        when(cardRepository.findByAccountId(accountId)).thenReturn(cards);

        List<Card> result = cardService.findByAccountId(accountId);

        assertEquals(2, result.size());
        verify(cardRepository, times(1)).findByAccountId(accountId);
    }
}