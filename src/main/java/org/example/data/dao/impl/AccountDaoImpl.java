package org.example.data.dao.impl;

import org.example.connection.DataSource;
import org.example.data.dao.AccountDao;
import org.example.entity.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountDaoImpl implements AccountDao {
    private static final Logger logger = LoggerFactory.getLogger(AccountDaoImpl.class);

    private static final String SQL_GET_BY_ID = "SELECT * FROM account WHERE account_id = ? AND deleted = FALSE";
    private static final String SQL_GET_ALL = "SELECT * FROM account WHERE deleted = FALSE";
    private static final String SQL_CREATE = "INSERT INTO account (account_number, account_balance, account_type, user_id, bank_id) VALUES (?, ?, ?, ?, ?)";
    private static final String SQL_UPDATE = "UPDATE account SET account_number = ?, account_balance = ?, account_type = ?, user_id = ?, bank_id = ? WHERE account_id = ? AND deleted = FALSE";
    private static final String SOFT_DELETE = "UPDATE account SET deleted = TRUE WHERE account_id = ? AND deleted = FALSE";
    private static final String SQL_GET_ALL_BY_USER_ID = "SELECT * FROM account WHERE user_id = ? AND deleted = FALSE";

    @Override
    public Account getById(Long id) {
        Account account = null;
        try (Connection connection = DataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_GET_BY_ID)) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    account = new Account();
                    createAccountFromResultSet(account, resultSet);
                }
            }
        } catch (SQLException e) {
            logger.error("Error getting account by id", e);
        }
        return account;
    }

    private void createAccountFromResultSet(Account account, ResultSet resultSet) throws SQLException {
        account.setAccountId(resultSet.getLong("account_id"));
        account.setAccountNumber(resultSet.getString("account_number"));
        account.setAccountBalance(resultSet.getDouble("account_balance"));
        account.setAccountType(resultSet.getString("account_type"));
        account.setUserId(resultSet.getLong("user_id"));
        account.setBankId(resultSet.getLong("bank_id"));
    }

    @Override
    public List<Account> getAll() {
        List<Account> accounts = new ArrayList<>();
        try (Connection connection = DataSource.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SQL_GET_ALL)) {
            createAccountFromResultSet(accounts, resultSet);
        } catch (SQLException e) {
            logger.error("Error getting all accounts", e);
        }
        return accounts;
    }

    private void createAccountFromResultSet(List<Account> accounts, ResultSet resultSet) throws SQLException {
        while (resultSet.next()) {
            Account account = new Account();
            createAccountFromResultSet(account, resultSet);
            accounts.add(account);
        }
    }

    @Override
    public Account create(Account account) {
        try (Connection connection = DataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(SQL_CREATE, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, account.getAccountNumber());
            statement.setDouble(2, account.getAccountBalance());
            statement.setString(3, account.getAccountType());
            statement.setLong(4, account.getUserId());
            statement.setLong(5, account.getBankId());
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet resultSet = statement.getGeneratedKeys()) {
                    if (resultSet.next()) {
                        Long id = resultSet.getLong(1);
                        account.setAccountId(id);
                    }
                }
            }
        } catch (SQLException e) {
            logger.error("Error creating account", e);
        }
        return account;
    }

    @Override
    public Account update(Account account) {
        try (Connection connection = DataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(SQL_UPDATE)) {
            statement.setString(1, account.getAccountNumber());
            statement.setDouble(2, account.getAccountBalance());
            statement.setString(3, account.getAccountType());
            statement.setLong(4, account.getUserId());
            statement.setLong(5, account.getBankId());
            statement.setLong(6, account.getAccountId());
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected == 0) {
                logger.warn("No rows updated for account with id {}", account.getAccountId());
            }
        } catch (SQLException e) {
            logger.error("Error updating account", e);
        }
        return account;
    }

    @Override
    public void delete(Account account) {
        try (Connection connection = DataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(SOFT_DELETE)) {
            statement.setLong(1, account.getAccountId());
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected == 0) {
                logger.warn("No rows deleted for account with id {}", account.getAccountId());
            }
        } catch (SQLException e) {
            logger.error("Error deleting account", e);
        }
    }

    @Override
    public List<Account> getAllByUserId(Long userId) {
        List<Account> accounts = new ArrayList<>();
        try (Connection connection = DataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_GET_ALL_BY_USER_ID)) {
            statement.setLong(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                createAccountFromResultSet(accounts, resultSet);
            }
        } catch (SQLException e) {
            logger.error("Error getting all accounts by user id", e);
        }
        return accounts;
    }
}
