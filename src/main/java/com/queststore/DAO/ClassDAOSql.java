package com.queststore.DAO;

import com.queststore.Model.Class;
import com.queststore.Model.ClassInfo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ClassDAOSql implements ClassDAO {

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

    @Override
    public List<ClassInfo> getClassesInfo() throws DaoException {
        try (Connection connection = DBCPDataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT classes.id AS id, classes.name AS name, " +
                            "SUM(CASE WHEN user_type_id = 2 THEN 1 ELSE 0 END) AS mentor_count, " +
                            "SUM(CASE WHEN user_type_id = 1 THEN 1 ELSE 0 END) AS student_count, " +
                            "classes.is_active " +
                            "FROM classes " +
                            "FULL OUTER JOIN users " +
                            "ON users.class_id = classes.id " +
//                            "WHERE users.is_active = true AND classes.is_active = true " +
                            "GROUP BY classes.id, classes.name"
            )) {
            List<ClassInfo> classInfos = new ArrayList<>();
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()) {
                classInfos.add(createClassInfo(resultSet));
            }
            return classInfos;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException("An error occur during parsing classes information");
        }
    }

    private Class createClass(ResultSet resultSet) throws SQLException {
        return new Class(resultSet.getInt("id"), resultSet.getString("name"));
    }

    private ClassInfo createClassInfo(ResultSet resultSet) throws SQLException {
        Class klass = createClass(resultSet);
        return new ClassInfo(klass,
                resultSet.getInt("mentor_count"),
                resultSet.getInt("student_count"),
                resultSet.getBoolean("is_active"));
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

    public List<String> getAllClassesName() throws DaoException {
        String SQL = "SELECT name FROM classes";
        List<String> classList = new ArrayList<>();
        try (Connection connection = DBCPDataSource.getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement(SQL);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                classList.add(resultSet.getString("name"));
            }
            return classList;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException("Issue wit Classes name list");
        }
    }
}
