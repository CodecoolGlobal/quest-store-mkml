package com.queststore.DAO;

import com.queststore.Model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class UserDAOSql implements UserDAO {

    public Optional<User> getUser(String email, String password) throws DaoException{
        try (Connection connection = DBCPDataSource.getConnection()){
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT id, first_name, last_name, email, classes.name AS class_name, avatar, user_type.name AS type " +
                            "FROM users " +
                            "JOIN user_type ON users.user_type_id = user_type.id " +
                            "JOIN classes ON users.class_id = classes.id " +
                            "WHERE email = ? AND password = ? AND is_active = true");
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

    private User createUser(ResultSet resultSet) throws SQLException {
        return new User.UserBuilder()
                .id(resultSet.getInt("id"))
                .firstName(resultSet.getString("firstname"))
                .lastName(resultSet.getString("lastname"))
                .email(resultSet.getString("email"))
                .classId(resultSet.getString("class_name"))
                .avatar(resultSet.getBlob("avatar"))
                .userType(resultSet.getString("type"))
                .createUser();
    }
}
