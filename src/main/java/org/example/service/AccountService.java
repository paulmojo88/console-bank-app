package org.example.service;

import org.example.data.dto.AccountDto;

import java.util.List;

public interface AccountService {
    AccountDto getAccountById(Long id);
    List<AccountDto> getAllAccounts();
    AccountDto createAccount(AccountDto accountDto);
    AccountDto updateAccount(AccountDto accountDto);
    void deleteAccount(AccountDto accountDto);
    List<AccountDto> getAllByUserId(Long userId);
    void transfer(AccountDto senderAccount, AccountDto recipientAccount, double amount);
    void withdraw(AccountDto account, double amount);
    void replenish(AccountDto account, double amount);
}
