package com.flowbank.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
public class AccountResponseDTO {
    private UUID id;
    private String accountNumber;
    private boolean blocked;
    private BigDecimal balance;
    private String ownerFullName;
    private List<CardResponseDTO> cards;
}
