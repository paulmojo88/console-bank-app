package org.example.data.dao.impl;

import org.example.connection.DataSource;
import org.example.data.dao.TransactionDao;
import org.example.entity.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionDaoImpl implements TransactionDao {
    private static final Logger logger = LoggerFactory.getLogger(TransactionDaoImpl.class);

    private static final String SQL_GET_BY_ID = "SELECT * FROM \"Transaction\" WHERE transaction_id = ? AND deleted = FALSE";
    private static final String SQL_GET_ALL = "SELECT * FROM \"Transaction\" WHERE deleted = FALSE";
    private static final String SQL_CREATE = "INSERT INTO \"Transaction\" (transaction_date, transaction_amount, transaction_type, account_id, recipient_account_id) VALUES (?, ?, ?, ?, ?)";
    private static final String SQL_UPDATE = "UPDATE \"Transaction\" SET transaction_date = ?, transaction_amount = ?, transaction_type = ?, account_id = ?, recipient_account_id = ?, check_number = ? WHERE transaction_id = ? AND deleted = FALSE";
    private static final String SOFT_DELETE = "UPDATE \"Transaction\" SET deleted = TRUE WHERE transaction_id = ? AND deleted = FALSE";

    @Override
    public Transaction getById(Long id) {
        Transaction transaction = null;
        try (Connection connection = DataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_GET_BY_ID)) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    transaction = new Transaction();
                    createTransactionFromResultSet(transaction, resultSet);
                }
            }
        } catch (SQLException e) {
            logger.error("Error getting transaction by id", e);
        }
        return transaction;
    }

    private void createTransactionFromResultSet(Transaction transaction, ResultSet resultSet) throws SQLException {
        transaction.setTransactionId(resultSet.getLong("transaction_id"));
        transaction.setTransactionDate(resultSet.getDate("transaction_date").toLocalDate());
        transaction.setTransactionAmount(resultSet.getDouble("transaction_amount"));
        transaction.setTransactionType(resultSet.getString("transaction_type"));
        transaction.setAccountId(resultSet.getLong("account_id"));
        transaction.setRecipientAccountId(resultSet.getLong("recipient_account_id"));
        transaction.setCheckNumber(resultSet.getLong("check_number"));
    }

    @Override
    public List<Transaction> getAll() {
        List<Transaction> transactions = new ArrayList<>();
        try (Connection connection = DataSource.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SQL_GET_ALL)) {
            while (resultSet.next()) {
                Transaction transaction = new Transaction();
                createTransactionFromResultSet(transaction, resultSet);
                transactions.add(transaction);
            }
        } catch (SQLException e) {
            logger.error("Error getting all transactions", e);
        }
        return transactions;
    }

    @Override
    public Transaction create(Transaction transaction) {
        try (Connection connection = DataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(SQL_CREATE, Statement.RETURN_GENERATED_KEYS)) {
            statement.setDate(1, Date.valueOf(transaction.getTransactionDate()));
            statement.setDouble(2, transaction.getTransactionAmount());
            statement.setString(3, transaction.getTransactionType());
            statement.setLong(4, transaction.getAccountId());
            statement.setLong(5, transaction.getRecipientAccountId());
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet resultSet = statement.getGeneratedKeys()) {
                    if (resultSet.next()) {
                        Long id = resultSet.getLong(1);
                        Long checkNumber = resultSet.getLong(7);
                        transaction.setTransactionId(id);
                        transaction.setCheckNumber(checkNumber);
                    }
                }
            }
        } catch (SQLException e) {
            logger.error("Error creating transaction", e);
        }
        return transaction;
    }

    @Override
    public Transaction update(Transaction transaction) {
        try (Connection connection = DataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(SQL_UPDATE)) {
            statement.setDate(1, Date.valueOf(transaction.getTransactionDate()));
            statement.setDouble(2, transaction.getTransactionAmount());
            statement.setString(3, transaction.getTransactionType());
            statement.setLong(4, transaction.getAccountId());
            statement.setLong(5, transaction.getRecipientAccountId());
            statement.setLong(6, transaction.getTransactionId());
            statement.setLong(7, transaction.getCheckNumber());
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected == 0) {
                logger.warn("No rows updated for transaction with id {}", transaction.getTransactionId());
            }
        } catch (SQLException e) {
            logger.error("Error updating transaction", e);
        }
        return transaction;
    }

    @Override
    public void delete(Transaction transaction) {
        try (Connection connection = DataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(SOFT_DELETE)) {
            statement.setLong(1, transaction.getTransactionId());
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected == 0) {
                logger.warn("No rows deleted for transaction with id {}", transaction.getTransactionId());
            }
        } catch (SQLException e) {
            logger.error("Error deleting transaction", e);
        }
    }
}
