package org.example.data.util;

import java.util.Random;

public class BankAccountNumberGenerator {
    public static final int BANK_ACCOUNT_NUMBER_LENGTH = 12;
    private static Random random = new Random();
    public static String generateBankAccountNumber() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < BANK_ACCOUNT_NUMBER_LENGTH; i++) {
            // Generate a random digit from 0 to 9
            int digit = random.nextInt(10);
            sb.append(digit);
        }
        return sb.toString();
    }

    public static String generateChecksumNumber() {
        int checksum = random.nextInt(90) + 10;
        return String.valueOf(checksum);
    }

    public static String generateIBAN() {
        StringBuilder sb = new StringBuilder();
        sb.append("BY");
        sb.append(generateChecksumNumber());
        sb.append("PRRB");
        sb.append("3012");
        sb.append(generateBankAccountNumber());
        return sb.toString();
    }
}
