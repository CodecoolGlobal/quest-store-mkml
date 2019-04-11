package com.queststore.DAO;

import com.queststore.Model.Class;
import com.queststore.Model.User;

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
            }
            for (User u :  dao.getStudentsFrom(1)) {
                System.out.println(u.getFirstName());
            }
        } catch (DaoException e) {
            e.printStackTrace();
        }

    }

    public Optional<User> getUser(String email, String password) throws DaoException{
        try (Connection connection = DBCPDataSource.getConnection()){
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT users.id as id, firstname, lastname, email, classes.name AS class_name, " +
                            "avatar, user_type.name AS type, classes.id as classId " +
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
    public User getUserById(int id) throws DaoException {
        String SQL = "SELECT * FROM  users WHERE id=?";
        ClassDAO classDAOSql = new ClassDAOSql();
        try (Connection conn = DBCPDataSource.getConnection()){
            PreparedStatement pstmt = conn.prepareStatement(SQL);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            rs.next();
            return new User(rs.getInt("id"), rs.getString("firstname"), rs.getString("lastname"), rs.getString("email")
            , classDAOSql.createClassFromId(rs.getInt("id")), null, rs.getString("user_type_id"));
            //TODO: userType powinien byc obiektem i Blob
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException("From getUserById cannot create user");
        }
    }

    public List<User> getStudentsFrom(int classId) throws DaoException {
        try (Connection connection = DBCPDataSource.getConnection()){
            List<User> students = new ArrayList<>();
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT users.id as id, firstname, lastname, email, classes.name AS class_name, " +
                            "avatar, user_type.name AS type, classes.id as classId " +
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



    private User createUser(ResultSet resultSet) throws SQLException {
        Class cls = new Class(resultSet.getInt("classId"), resultSet.getString("class_name"));
        return new User.UserBuilder()
                .id(resultSet.getInt("id"))
                .firstName(resultSet.getString("firstname"))
                .lastName(resultSet.getString("lastname"))
                .email(resultSet.getString("email"))
                .userClass(cls)
//                .avatar(resultSet.getBlob("avatar"))
                .userType(resultSet.getString("type"))
                .createUser();
    }
}
