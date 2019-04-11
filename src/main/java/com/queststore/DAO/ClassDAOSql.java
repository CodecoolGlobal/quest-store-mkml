package com.queststore.DAO;

import com.queststore.Model.Class;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClassDAOSql implements ClassDAO {

    public static void main(String[] args) {
        ClassDAO dao = new ClassDAOSql();
        try {
            for (Class c : dao.getAllClasses()) {
                System.out.println(c.getName());
            }
        } catch (DaoException e) {
            e.printStackTrace();
        }

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

    public Class createClassFromId(int id) throws DaoException{
        String SQL = "SELECT * FROM classes WHERE id = ?";
        try (Connection connection = DBCPDataSource.getConnection()){
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



    public Class createClass(ResultSet resultSet) throws SQLException {
        return new Class(resultSet.getInt("id"), resultSet.getString("name"));
    }

}
