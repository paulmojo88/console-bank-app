package org.example.data.dao.impl;

import org.example.connection.DataSource;
import org.example.data.dao.UserDao;
import org.example.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoImpl implements UserDao {
    private static final Logger logger = LoggerFactory.getLogger(UserDaoImpl.class);

    private static final String SQL_GET_BY_ID = "SELECT * FROM \"User\" WHERE user_id = ? AND deleted = FALSE";
    private static final String SQL_GET_ALL = "SELECT * FROM \"User\" WHERE deleted = FALSE";
    private static final String SQL_CREATE = "INSERT INTO \"User\" (user_name, user_email, user_phone) VALUES (?, ?, ?)";
    private static final String SQL_UPDATE = "UPDATE \"User\" SET user_name = ?, user_email = ?, user_phone = ? WHERE user_id = ? AND deleted = FALSE";
    private static final String SOFT_DELETE = "UPDATE \"User\" SET deleted = TRUE WHERE user_id = ? AND deleted = FALSE";

    @Override
    public User getById(Long id) {
        User user = null;
        try (Connection connection = DataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_GET_BY_ID)) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    user = new User();
                    user.setUserId(resultSet.getLong("user_id"));
                    user.setUserName(resultSet.getString("user_name"));
                    user.setUserEmail(resultSet.getString("user_email"));
                    user.setUserPhone(resultSet.getString("user_phone"));
                }
            }
        } catch (SQLException e) {
            logger.error("Error getting user by id", e);
        }
        return user;
    }

    @Override
    public List<User> getAll() {
        List<User> users = new ArrayList<>();
        try (Connection connection = DataSource.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SQL_GET_ALL)) {
            while (resultSet.next()) {
                User user = new User();
                user.setUserId(resultSet.getLong("user_id"));
                user.setUserName(resultSet.getString("user_name"));
                user.setUserEmail(resultSet.getString("user_email"));
                user.setUserPhone(resultSet.getString("user_phone"));
                users.add(user);
            }
        } catch (SQLException e) {
            logger.error("Error getting all users", e);
        }
        return users;
    }

    @Override
    public User create(User user) {
        try (Connection connection = DataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(SQL_CREATE, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, user.getUserName());
            statement.setString(2, user.getUserEmail());
            statement.setString(3, user.getUserPhone());
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet resultSet = statement.getGeneratedKeys()) {
                    if (resultSet.next()) {
                        Long id = resultSet.getLong(1);
                        user.setUserId(id);
                    }
                }
            }
        } catch (SQLException e) {
            logger.error("Error creating user", e);
        }
        return user;
    }

    @Override
    public User update(User user) {
        try (Connection connection = DataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(SQL_UPDATE)) {
            statement.setString(1, user.getUserName());
            statement.setString(2, user.getUserEmail());
            statement.setString(3, user.getUserPhone());
            statement.setLong(4, user.getUserId());
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected == 0) {
                logger.warn("No rows updated for user with id {}", user.getUserId());
            }
        } catch (SQLException e) {
            logger.error("Error updating user", e);
        }
        return user;
    }

    @Override
    public void delete(User user) {
        try (Connection connection = DataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(SOFT_DELETE)) {
            statement.setLong(1, user.getUserId());
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected == 0) {
                logger.warn("No rows deleted for user with id {}", user.getUserId());
            }
        } catch (SQLException e) {
            logger.error("Error deleting user", e);
        }
    }
}
