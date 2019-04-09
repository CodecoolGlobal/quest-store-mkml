package com.queststore.DAO;

import com.queststore.Model.User;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class UserDAOSql implements UserDAO {

    public Optional<User> getUser(String email, String password) throws DaoException{
        try {
            Connection connection = DBCPDataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM users WHERE email = ? AND password = ?");
            statement.setString(1, email);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(createUser(resultSet));
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new DaoException("An error occured during getting user from db");
        }
    }

    private User createUser(ResultSet resultSet) {
        return null;
    }
}
