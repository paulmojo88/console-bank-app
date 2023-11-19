package org.example.data.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class TransactionDto {
    private Long transactionId;
    private LocalDate transactionDate;
    private Double transactionAmount;
    private String transactionType;
    private Long accountId;
    private Long recipientAccountId;
    private Long checkNumber;
}
