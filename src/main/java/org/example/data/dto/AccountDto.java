package org.example.data.dto;

import lombok.Data;

@Data
public class AccountDto {
    private Long accountId;
    private String accountNumber;
    private Double accountBalance;
    private String accountType;
    private Long userId;
    private Long bankId;

    public String showAccountInfo() {
        return "accountId=" + accountId +
                ", accountNumber='" + accountNumber + '\'' +
                ", accountBalance=" + accountBalance +
                ", accountType='" + accountType + '\'';
    }
}
