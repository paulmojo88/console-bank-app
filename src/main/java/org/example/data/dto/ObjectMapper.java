package org.example.data.dto;

import org.example.entity.Account;
import org.example.entity.Bank;
import org.example.entity.Transaction;
import org.example.entity.User;

public class ObjectMapper {
    public static AccountDto toDto(Account account) {
        AccountDto accountDto = new AccountDto();
        accountDto.setAccountId(account.getAccountId());
        accountDto.setAccountNumber(account.getAccountNumber());
        accountDto.setAccountBalance(account.getAccountBalance());
        accountDto.setAccountType(account.getAccountType());
        accountDto.setUserId(account.getUserId());
        accountDto.setBankId(account.getBankId());
        return accountDto;
    }

    public static Account toEntity(AccountDto accountDto) {
        Account account = new Account();
        account.setAccountId(accountDto.getAccountId());
        account.setAccountNumber(accountDto.getAccountNumber());
        account.setAccountBalance(accountDto.getAccountBalance());
        account.setAccountType(accountDto.getAccountType());
        account.setUserId(accountDto.getUserId());
        account.setBankId(accountDto.getBankId());
        return account;
    }

    public static BankDto toDto(Bank bank) {
        BankDto bankDto = new BankDto();
        bankDto.setBankId(bank.getBankId());
        bankDto.setBankName(bank.getBankName());
        bankDto.setBankAddress(bank.getBankAddress());
        return bankDto;
    }

    public static Bank toEntity(BankDto bankDto) {
        Bank bank = new Bank();
        bank.setBankId(bankDto.getBankId());
        bank.setBankName(bankDto.getBankName());
        bank.setBankAddress(bankDto.getBankAddress());
        return bank;
    }

    public static TransactionDto toDto(Transaction transaction) {
        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setTransactionId(transaction.getTransactionId());
        transactionDto.setTransactionDate(transaction.getTransactionDate());
        transactionDto.setTransactionAmount(transaction.getTransactionAmount());
        transactionDto.setTransactionType(transaction.getTransactionType());
        transactionDto.setAccountId(transaction.getAccountId());
        transactionDto.setRecipientAccountId(transaction.getRecipientAccountId());
        transactionDto.setCheckNumber(transaction.getCheckNumber());
        return transactionDto;
    }

    public static Transaction toEntity(TransactionDto transactionDto) {
        Transaction transaction = new Transaction();
        transaction.setTransactionId(transactionDto.getTransactionId());
        transaction.setTransactionDate(transactionDto.getTransactionDate());
        transaction.setTransactionAmount(transactionDto.getTransactionAmount());
        transaction.setTransactionType(transactionDto.getTransactionType());
        transaction.setAccountId(transactionDto.getAccountId());
        transaction.setRecipientAccountId(transactionDto.getRecipientAccountId());
        transaction.setCheckNumber(transactionDto.getCheckNumber());
        return transaction;
    }

    public static UserDto toDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setUserId(user.getUserId());
        userDto.setUserName(user.getUserName());
        userDto.setUserEmail(user.getUserEmail());
        userDto.setUserPhone(user.getUserPhone());
        return userDto;
    }

    public static User toEntity(UserDto userDto) {
        User user = new User();
        user.setUserId(userDto.getUserId());
        user.setUserName(userDto.getUserName());
        user.setUserEmail(userDto.getUserEmail());
        user.setUserPhone(userDto.getUserPhone());
        return user;
    }
}
