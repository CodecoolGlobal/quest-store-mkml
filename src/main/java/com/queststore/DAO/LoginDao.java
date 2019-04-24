package com.queststore.DAO;


import com.queststore.Controller.PasswordHasher;
import com.queststore.Model.LoginUser;

import java.net.HttpCookie;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Optional;

public class LoginDao {

    private static PasswordHasher passwordHasher = new PasswordHasher();

    public static void main(String[] args) {
//        try (Connection connection = DBCPDataSource.getConnection();
//             PreparedStatement statement = connection.prepareStatement(
//                     "INSERT INTO users(login, password, salt) VALUES (?, ?, ?);"
//             )) {
//            byte[] salt = passwordHasher.getSalt();
//            statement.setString(1, "kamil");
//            statement.setBytes(2, passwordHasher.getHashed("bed", salt));
//            statement.setBytes(3, salt);
//            statement.execute();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    public Optional<Integer> getUserId(LoginUser userToValidate) throws DaoException {
        try (Connection connection = DBCPDataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT password, id FROM users WHERE email = ?;"
             )) {
            statement.setString(1, userToValidate.getEmail());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                byte[] passwordWithSalt = resultSet.getBytes("password");
                byte[] password = Arrays.copyOfRange(passwordWithSalt, 0, 16);
                byte[] salt = Arrays.copyOfRange(passwordWithSalt, 16, 32);
                byte[] passwordToValidate = passwordHasher.getHashed(userToValidate.getPassword(), salt);
                System.out.println(new String(password) + " " + new String(passwordToValidate));
                return Arrays.equals(password, passwordToValidate)
                        ? Optional.of(resultSet.getInt("id")) : Optional.empty();
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException("Error occured during checking user data");
        } catch (Exception e) {
            throw new DaoException("Cannot hash pasword");
        }
    }

    public void saveSession(HttpCookie cookie, Integer userId) throws DaoException {
        try (Connection connection = DBCPDataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "INSERT INTO sessions(session_id, user_id) VALUES (?, ?);"
             )) {
            System.out.println(cookie.getValue());
            statement.setString(1, cookie.getValue());
            statement.setInt(2, userId);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException("Error occured during adding new cookie");
        }
    }

    public void deleteSessionCookie(HttpCookie cookie) throws DaoException {
        try (Connection connection = DBCPDataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "DELETE FROM sessions WHERE session_id = ?;"
             )) {
            statement.setString(1, cookie.getValue());
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException("Error occured during deleting session cookie");
        }
    }
}
