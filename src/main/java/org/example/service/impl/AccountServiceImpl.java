package org.example.service.impl;

import org.example.data.dto.AccountDto;
import org.example.service.AccountService;

import java.util.List;

public class AccountServiceImpl implements AccountService {
    @Override
    public AccountDto getAccountById(Long id) {
        return null;
    }

    @Override
    public List<AccountDto> getAllAccounts() {
        return null;
    }

    @Override
    public AccountDto createAccount(AccountDto accountDto) {
        return null;
    }

    @Override
    public AccountDto updateAccount(AccountDto accountDto) {
        return null;
    }

    @Override
    public void deleteAccount(AccountDto accountDto) {

    }

    @Override
    public List<AccountDto> getAllByUserId(Long userId) {
        return null;
    }

    @Override
    public void transfer(AccountDto senderAccount, AccountDto recipientAccount, double amount) {

    }

    @Override
    public void withdraw(AccountDto account, double amount) {

    }

    @Override
    public void replenish(AccountDto account, double amount) {

    }
}
