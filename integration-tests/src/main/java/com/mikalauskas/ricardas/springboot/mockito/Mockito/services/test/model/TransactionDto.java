package com.mikalauskas.ricardas.springboot.mockito.Mockito.services.test.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransactionDto {
    private Long originAccountId;
    private Long destinationAccountId;
    private BigDecimal amount;
    private Long bankId;
}
