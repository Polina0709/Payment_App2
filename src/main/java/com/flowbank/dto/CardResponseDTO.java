package com.flowbank.dto;

import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CardResponseDTO {
    private String cardNumber;
    private LocalDate expirationDate;
}