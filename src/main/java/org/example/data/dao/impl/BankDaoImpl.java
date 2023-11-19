package org.example.data.dao.impl;

import org.example.connection.DataSource;
import org.example.data.dao.BankDao;
import org.example.entity.Bank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BankDaoImpl implements BankDao {
    private static final Logger logger = LoggerFactory.getLogger(BankDaoImpl.class);

    private static final String SQL_GET_BY_ID = "SELECT * FROM bank WHERE bank_id = ?";
    private static final String SQL_GET_ALL = "SELECT * FROM bank";
    private static final String SQL_CREATE = "INSERT INTO bank (bank_name, bank_address) VALUES (?, ?)";
    private static final String SQL_UPDATE = "UPDATE bank SET bank_name = ?, bank_address = ? WHERE bank_id = ?";
    private static final String SOFT_DELETE = "UPDATE bank SET deleted = TRUE WHERE bank_id = ? AND deleted = FALSE";

    @Override
    public Bank getById(Long id) {
        Bank bank = null;
        try (Connection connection = DataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_GET_BY_ID)) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    bank = new Bank();
                    bank.setBankId(resultSet.getLong("bank_id"));
                    bank.setBankName(resultSet.getString("bank_name"));
                    bank.setBankAddress(resultSet.getString("bank_address"));
                }
            }
        } catch (SQLException e) {
            logger.error("Error getting bank by id", e);
        }
        return bank;
    }

    @Override
    public List<Bank> getAll() {
        List<Bank> banks = new ArrayList<>();
        try (Connection connection = DataSource.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SQL_GET_ALL)) {
            while (resultSet.next()) {
                Bank bank = new Bank();
                bank.setBankId(resultSet.getLong("bank_id"));
                bank.setBankName(resultSet.getString("bank_name"));
                bank.setBankAddress(resultSet.getString("bank_address"));
                banks.add(bank);
            }
        } catch (SQLException e) {
            logger.error("Error getting all banks", e);
        }
        return banks;
    }

    @Override
    public Bank create(Bank bank) {
        try (Connection connection = DataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(SQL_CREATE, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, bank.getBankName());
            statement.setString(2, bank.getBankAddress());
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet resultSet = statement.getGeneratedKeys()) {
                    if (resultSet.next()) {
                        Long id = resultSet.getLong(1);
                        bank.setBankId(id);
                    }
                }
            }
        } catch (SQLException e) {
            logger.error("Error creating bank", e);
        }
        return bank;
    }

    @Override
    public Bank update(Bank bank) {
        try (Connection connection = DataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(SQL_UPDATE)) {
            statement.setString(1, bank.getBankName());
            statement.setString(2, bank.getBankAddress());
            statement.setLong(3, bank.getBankId());
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected == 0) {
                logger.warn("No rows updated for bank with id {}", bank.getBankId());
            }
        } catch (SQLException e) {
            logger.error("Error updating bank", e);
        }
        return bank;
    }

    @Override
    public void delete(Bank bank) {
        try (Connection connection = DataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(SOFT_DELETE)) {
            statement.setLong(1, bank.getBankId());
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected == 0) {
                logger.warn("No rows deleted for bank with id {}", bank.getBankId());
            }
        } catch (SQLException e) {
            logger.error("Error deleting bank", e);
        }
    }
}
