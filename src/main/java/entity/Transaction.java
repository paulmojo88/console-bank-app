package entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class Transaction {
    private Long transactionId;
    private LocalDate transactionDate;
    private Double transactionAmount;
    private String transactionType;
    private Long accountId;
    private Long recipientAccountId;
    private Long checkNumber;

    public Transaction(LocalDate transactionDate, Double transactionAmount, String transactionType, Long accountId, Long recipientAccountId, Long checkNumber) {
        this.transactionDate = transactionDate;
        this.transactionAmount = transactionAmount;
        this.transactionType = transactionType;
        this.accountId = accountId;
        this.recipientAccountId = recipientAccountId;
        this.checkNumber = checkNumber;
    }
}
