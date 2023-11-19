package org.example.data.dao;

import org.example.data.dao.impl.AccountDaoImpl;
import org.example.data.dao.impl.BankDaoImpl;
import org.example.data.dao.impl.TransactionDaoImpl;
import org.example.data.dao.impl.UserDaoImpl;

public class DaoFactory {

    private static DaoFactory instance;

    private DaoFactory() {
    }

    public static DaoFactory getInstance() {
        if (instance == null) {
            instance = new DaoFactory();
        }
        return instance;
    }

    public AccountDao getAccountDao() {
        return new AccountDaoImpl();
    }

    public BankDao getBankDao() {
        return new BankDaoImpl();
    }

    public TransactionDao getTransactionDao() {
        return new TransactionDaoImpl();
    }

    public UserDao getUserDao() {
        return new UserDaoImpl();
    }
}
