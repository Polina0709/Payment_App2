package com.flowbank.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransferRequestDTO {
    private String senderAccountNumber;
    private String receiverAccountNumber;
    private BigDecimal amount;
}

