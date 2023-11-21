package org.example.service.impl;

import org.example.data.dao.AccountDao;
import org.example.data.dao.BankDao;
import org.example.data.dao.DaoFactory;
import org.example.data.dao.TransactionDao;
import org.example.data.dto.AccountDto;
import org.example.data.util.ObjectMapper;
import org.example.entity.Account;
import org.example.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class AccountServiceImpl implements AccountService {
    private static final Logger logger = LoggerFactory.getLogger(AccountService.class);
    private static final String CONFIG_FILE = "src/main/resources/config.yml";
    private static final String RECEIPT_FOLDER = "src/main/checks";
    private static final String RECEIPT_FORMAT = "check_%s_%s.pdf";
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final String TIME_FORMAT = "HH:mm:ss";
    private static final String INTEREST_RATE_KEY = "interest_rate";

    private AccountDao accountDao;
    private BankDao bankDao;
    private TransactionDao transactionDao;

    public AccountServiceImpl() {
        DaoFactory daoFactory = DaoFactory.getInstance();
        if (accountDao == null) {
            accountDao = daoFactory.getAccountDao();
        }
        if (bankDao == null) {
            bankDao = daoFactory.getBankDao();
        }
        if (transactionDao == null) {
            transactionDao = daoFactory.getTransactionDao();
        }
    }


    @Override
    public AccountDto getAccountById(Long id) {
        Account account = accountDao.getById(id);
        if (account == null) {
            return null;
        }
        return ObjectMapper.toDto(account);

    }

    @Override
    public List<AccountDto> getAllAccounts() {
        List<Account> accounts = accountDao.getAll();
        List<AccountDto> accountDtos = new ArrayList<>();
        for (Account account : accounts) {
            accountDtos.add(ObjectMapper.toDto(account));
        }
        return accountDtos;
    }

    @Override
    public AccountDto createAccount(AccountDto accountDto) {
        Account account = ObjectMapper.toEntity(accountDto);
        account = accountDao.create(account);
        return ObjectMapper.toDto(account);
    }

    @Override
    public AccountDto updateAccount(AccountDto accountDto) {
        Account account = ObjectMapper.toEntity(accountDto);
        account = accountDao.update(account);
        return ObjectMapper.toDto(account);
    }


    @Override
    public void deleteAccount(AccountDto accountDto) {
        Account account = ObjectMapper.toEntity(accountDto);
        accountDao.delete(account);
    }

    @Override
    public List<AccountDto> getAllByUserId(Long userId) {
        List<Account> accounts = accountDao.getAllByUserId(userId);
        List<AccountDto> accountDtos = new ArrayList<>();
        for (Account account : accounts) {
            accountDtos.add(ObjectMapper.toDto(account));
        }
        return accountDtos;
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
