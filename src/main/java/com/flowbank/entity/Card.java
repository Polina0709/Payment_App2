package com.flowbank.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "cards")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Card {

    @Id
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @Column(name = "card_number", unique = true)
    private String cardNumber;

    @Column(name = "expiration_date")
    private LocalDate expirationDate;

    private String cvv;
}
