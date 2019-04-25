package com.queststore.DAO;

import com.queststore.Model.Class;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ClassDAOSql implements ClassDAO {

    public static void main(String[] args) {
        ClassDAO dao = new ClassDAOSql();
        try {
//            dao.add("myclass");
            for (Class c : dao.getAllClasses()) {
                System.out.println(c.getName());
                System.out.println(dao.getStudentsCountByClassId(c.getId()));
//                c.setName("newClass");
//                dao.update(c);
//                dao.delete(c.getId());
            }
        } catch (DaoException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getMentorsCountByClassId(int id) throws DaoException {
        return getUserCountByClassId(id, "mentor");
    }

    @Override
    public int getStudentsCountByClassId(int id) throws DaoException {
        return getUserCountByClassId(id, "student");
    }

    @Override
    public List<Class> getAllClasses() throws DaoException {
        try (Connection connection = DBCPDataSource.getConnection()) {
            List<Class> classes = new ArrayList<>();
            PreparedStatement statement = connection.prepareStatement("SELECT id, name FROM classes " +
                    "WHERE is_active = true;");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                classes.add(createClass(resultSet));
            }
            return classes;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException("An error occured during getting all classes from db");
        }
    }

    @Override
    public List<Class> getAllClasses(int mentorId) throws DaoException {
        try (Connection connection = DBCPDataSource.getConnection()) {
            List<Class> classes = new ArrayList<>();
            PreparedStatement statement = connection.prepareStatement("SELECT classes.id, classes.name FROM classes " +
                    "JOIN users " +
                    "ON classes.id = users.class_id " +
                    "WHERE classes.is_active = true AND users.id = ?;");
            statement.setInt(1, mentorId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                classes.add(createClass(resultSet));
            }
            return classes;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException("An error occured during getting all classes from db");
        }
    }

    @Override
    public void add(String name) throws DaoException {
        try (Connection connection = DBCPDataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO classes (" +
                    "name, is_active) " +
                    "VALUES (?, true);");
            statement.setString(1, name);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException("An error occured during adding new class");
        }
    }

    @Override
    public void update(Class cls) throws DaoException {
        try (Connection connection = DBCPDataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("UPDATE classes SET " +
                    "name = ? " +
                    "WHERE id = ?;");
            statement.setString(1, cls.getName());
            statement.setInt(2, cls.getId());
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException("An error occured during updating class");
        }
    }

    @Override
    public void delete(int classId) throws DaoException {
        try (Connection connection = DBCPDataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("UPDATE classes SET is_active = false " +
                    "WHERE id = ?;");
            statement.setInt(1, classId);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException("An error occured during deleting class");
        }
    }

    public Class createClassFromId(int id) throws DaoException {
        String SQL = "SELECT * FROM classes WHERE id = ?";
        try (Connection connection = DBCPDataSource.getConnection()) {
            PreparedStatement pstmt = connection.prepareStatement(SQL);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            rs.next();
            return createClass(rs);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException("An error occured during getting class by id from db");

        }
    }

    public Optional<Integer> getClassId(String className) throws  DaoException {
        String SQL = "SELECT id FROM classes WHERE name=?";
        try (Connection connection = DBCPDataSource.getConnection()) {
            PreparedStatement pstmt = connection.prepareStatement(SQL);
            pstmt.setString(1, className);
            ResultSet resultSet = pstmt.executeQuery();
            return resultSet.next() ? Optional.of(resultSet.getInt("id")) : Optional.empty();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException("getClassId something wrong");
        }
    }

    private Class createClass(ResultSet resultSet) throws SQLException {
        return new Class(resultSet.getInt("id"), resultSet.getString("name"));
    }

    private int getUserCountByClassId(int id, String userType) throws DaoException {
        try (Connection connection = DBCPDataSource.getConnection()) {
            int count = 0;
            PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) AS count FROM users " +
                    "JOIN user_type " +
                    "ON user_type.id = users.user_type_id " +
                    "WHERE class_id = ? AND is_active = true AND user_type.name = ?");
            statement.setInt(1, id);
            statement.setString(2, userType);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                count = resultSet.getInt("count");
            }
            return count;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException("An error occured during getting user count for specific class");
        }
    }

}
