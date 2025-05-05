package com.flowbank.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TopUpRequestDTO {
    private String accountNumber;
    private BigDecimal amount;
}
