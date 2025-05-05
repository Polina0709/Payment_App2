package com.flowbank.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class TransactionResponseDTO {
    private UUID id;
    private UUID senderAccountId;
    private UUID receiverAccountId;
    private BigDecimal amount;
    private LocalDateTime createdAt;
}

