package com.queststore.DAO;

import com.queststore.Controller.PasswordHasher;
import com.queststore.Model.Class;
import com.queststore.Model.User;
import com.queststore.Model.UserType;

import java.io.ByteArrayOutputStream;
import java.net.HttpCookie;
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
            dao.add(new User(1,"Admin", "Nazwisko", "admin@admin", new Class(1, "java"),
                    null, new UserType(1, "admin")), "kamil");
        } catch (DaoException e) {
            e.printStackTrace();
        }
    }

    //just for testing, delete later
    public void setTestPassword() throws DaoException {
        try (Connection connection = DBCPDataSource.getConnection()){
            PreparedStatement statement = connection.prepareStatement(
                    "UPDATE users SET password = ? WHERE id = 2");
            PasswordHasher ph = new PasswordHasher();
            byte[] salt = ph.getSalt();
            byte[] password = ph.getHashed("kamil", salt);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
            outputStream.write(password);
            outputStream.write(salt);
            byte[] passSalt = outputStream.toByteArray();
            statement.setBytes(1, passSalt);
            statement.execute();
        } catch (Exception e) {
            e.printStackTrace();
            throw new DaoException("An error occured during getting user from db");
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
            return resultSet.next() ? Optional.of(createUser(resultSet)) : Optional.empty();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException("An error occured during getting user from db");
        }
    }



    @Override
    public Optional<User> getUserById(int id) throws DaoException {
        String SQL = "SELECT users.id as id, firstname, lastname, email, classes.name AS class_name, " +
                "avatar, user_type.name AS type, user_type.id as typeId, classes.id as classId " +
                "FROM users " +
                "JOIN user_type ON users.user_type_id = user_type.id " +
                "JOIN classes ON users.class_id = classes.id " +
                "WHERE users.id = ? AND users.is_active = true";
        try (Connection conn = DBCPDataSource.getConnection()){
            PreparedStatement pstmt = conn.prepareStatement(SQL);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            return rs.next() ? Optional.of(createUser(rs)) : Optional.empty();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException("From getUserById cannot create user");
        }
    }

    @Override
    public UserType getUserTypeFromId (int id) throws DaoException{
        String SQL = "SELECT * FROM user_type WHERE id = ?";
        try (Connection connection = DBCPDataSource.getConnection()){
            PreparedStatement pstmt = connection.prepareStatement(SQL);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            rs.next();
            return createUserTypeObject(rs);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException("An error occured during getting user type from db");
        }
    }

    private UserType createUserTypeObject(ResultSet rs) throws SQLException {
        return new UserType(rs.getInt("id"), rs.getString("name"));

    }

    @Override
    public List<User> getAllUser(String userType) throws DaoException {
        String SQL = "SELECT users.id as id, firstname, lastname, email, classes.name AS class_name, " +
                "avatar, user_type.name AS type, user_type.id as typeId, classes.id as classId " +
                "FROM users " +
                "JOIN user_type ON users.user_type_id = user_type.id " +
                "JOIN classes ON users.class_id = classes.id " +
                "WHERE user_type.name = '"+ userType +"' AND users.is_active = true";
        try (Connection connection = DBCPDataSource.getConnection()){
            List<User> mentors = new ArrayList<>();
            PreparedStatement pstmt = connection.prepareStatement(SQL);
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()){
                mentors.add(createUser(rs));
            }
            return mentors;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException("An error occured during paring all mentors from db");
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
            statement.setBytes(6, createRandomPassword(password));
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException("An error occured during adding new user");
        }catch (Exception ex){
            ex.printStackTrace();
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

    @Override
    public void delete(int userId) throws DaoException {
        try (Connection connection = DBCPDataSource.getConnection()){
            PreparedStatement statement = connection.prepareStatement("UPDATE users SET is_active = false " +
                    "WHERE id = ?;");
            statement.setInt(1, userId);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException("An error occured during deleting user");
        }
    }

    @Override
    public Optional<User> getUserOfSession(HttpCookie cookie) throws DaoException {
        try (Connection connection = DBCPDataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT users.id as id, firstname, lastname, email, classes.name AS class_name, " +
                             "avatar, user_type.name AS type, user_type.id as typeId, classes.id as classId " +
                             "FROM users " +
                             "JOIN user_type ON users.user_type_id = user_type.id " +
                             "JOIN classes ON users.class_id = classes.id " +
                             "JOIN sessions ON users.id = sessions.user_id " +
                             "WHERE sessions.session_id = ? AND users.is_active = true;"
             )) {
            statement.setString(1, cookie.getValue());
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next() ? Optional.of(createUser(resultSet)) : Optional.empty();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException("Error occured during getting user of session");
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

    private byte[] createRandomPassword(String newPassword) throws Exception {
        PasswordHasher ph = new PasswordHasher();
        byte[] salt = ph.getSalt();
        byte[] password = ph.getHashed(newPassword, salt);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
        outputStream.write(password);
        outputStream.write(salt);
        byte[] passSalt = outputStream.toByteArray();
        return passSalt;
    }
}
