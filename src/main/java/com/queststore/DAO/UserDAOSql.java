package com.queststore.DAO;

import com.queststore.Model.Class;
import com.queststore.Model.User;
import com.queststore.Model.UserType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDAOSql implements UserDAO {

    public static void main(String[] args) {
        UserDAO dao = new UserDAOSql();
        try {
            Optional<User> user = dao.getUser("kamil@bed", "asdfsdf");
            if (user.isPresent()) {
                System.out.println(user.get().getFirstName());
                System.out.println(user.get().getUserClass().getName());
                User u = user.get();
                u.setFirstName("Karararumba");
                dao.update(u);
                u.setFirstName("Lama");
                u.setEmail("dfdfd@dfdfd");
                dao.add(u, "olalalal");
            }
            for (User u :  dao.getStudentsFrom(1)) {
                System.out.println(u.getFirstName());
            }
        } catch (DaoException e) {
            e.printStackTrace();
        }

    }

    @Override
    public Optional<User> getUser(String email, String password) throws DaoException{
        try (Connection connection = DBCPDataSource.getConnection()){
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT users.id as id, firstname, lastname, email, classes.name AS class_name, " +
                            "avatar, user_type.name AS type, user_type.id as typeId, classes.id as classId " +
                            "FROM users " +
                            "JOIN user_type ON users.user_type_id = user_type.id " +
                            "JOIN classes ON users.class_id = classes.id " +
                            "WHERE email = ? AND password = ? AND users.is_active = true");
            statement.setString(1, email);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(createUser(resultSet));
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException("An error occured during getting user from db");
        }
    }

    @Override
    public List<User> getStudentsFrom(int classId) throws DaoException {
        try (Connection connection = DBCPDataSource.getConnection()){
            List<User> students = new ArrayList<>();
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT users.id as id, firstname, lastname, email, classes.name AS class_name, " +
                            "avatar, user_type.name AS type, user_type.id as typeId, classes.id as classId " +
                            "FROM users " +
                            "JOIN user_type ON users.user_type_id = user_type.id " +
                            "JOIN classes ON users.class_id = classes.id " +
                            "WHERE classes.id = ? AND user_type.name = 'student' AND users.is_active = true");
            statement.setInt(1, classId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                students.add(createUser(resultSet));
            }
            return students;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException("An error occured during getting students from class from db");
        }
    }

    @Override
    public void add(User user, String password) throws DaoException {
        try (Connection connection = DBCPDataSource.getConnection()){
            PreparedStatement statement = connection.prepareStatement("INSERT INTO users (" +
                    "firstname, lastname, email, class_id, avatar, user_type_id, is_active, password) " +
                    "VALUES (?, ?, ?, ?, 'avatar', ?, true, ?);");
            statement.setString(1, user.getFirstName());
            statement.setString(2, user.getLastName());
            statement.setString(3, user.getEmail());
            statement.setInt(4, user.getUserClass().getId());
            statement.setInt(5, user.getUserType().getId());
            statement.setString(6, password);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException("An error occured during adding new user");
        }
    }

    @Override
    public void update(User user) throws DaoException {
        try (Connection connection = DBCPDataSource.getConnection()){
            PreparedStatement statement = connection.prepareStatement("UPDATE users SET " +
                    "firstname = ?, lastname = ?, email = ?, class_id = ? " +
                    "WHERE id = ?;");
            statement.setString(1, user.getFirstName());
            statement.setString(2, user.getLastName());
            statement.setString(3, user.getEmail());
            statement.setInt(4, user.getUserClass().getId());
            statement.setInt(5, user.getId());
            //leave avatar be
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException("An error occured during updating user");
        }
    }

    private User createUser(ResultSet resultSet) throws SQLException {
        Class cls = new Class(resultSet.getInt("classId"), resultSet.getString("class_name"));
        UserType userType = new UserType(resultSet.getInt("typeId"), resultSet.getString("type"));
        return new User.UserBuilder()
                .id(resultSet.getInt("id"))
                .firstName(resultSet.getString("firstname"))
                .lastName(resultSet.getString("lastname"))
                .email(resultSet.getString("email"))
                .userClass(cls)
//                .avatar(resultSet.getBlob("avatar"))
                .userType(userType)
                .createUser();
    }
}
