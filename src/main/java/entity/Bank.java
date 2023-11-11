package entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Bank {
    private Long bankId;
    private String bankName;
    private String bankAddress;

    public Bank(String bankName, String bankAddress) {
        this.bankName = bankName;
        this.bankAddress = bankAddress;
    }
}
