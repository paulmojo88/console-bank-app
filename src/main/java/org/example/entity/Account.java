package org.example.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Account {
    private Long accountId;
    private String accountNumber;
    private Double accountBalance;
    private String accountType;
    private Long userId;
    private Long bankId;

    public Account(String accountNumber, Double accountBalance, String accountType, Long userId, Long bankId) {
        this.accountNumber = accountNumber;
        this.accountBalance = accountBalance;
        this.accountType = accountType;
        this.userId = userId;
        this.bankId = bankId;
    }
}
