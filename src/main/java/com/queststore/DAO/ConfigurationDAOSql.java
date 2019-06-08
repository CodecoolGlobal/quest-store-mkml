package com.queststore.DAO;

import com.queststore.Model.ExperienceLevel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ConfigurationDAOSql implements ConfigurationDAO {

    @Override
    public List<ExperienceLevel> getAllLevels() throws DaoException {

        List<ExperienceLevel> experienceLevelList = new ArrayList<>();
        String SQL = "SELECT * FROM levels";
        try (Connection connection = DBCPDataSource.getConnection()) {
            PreparedStatement pstmt = connection.prepareStatement(SQL);
            ResultSet resultSet = pstmt.executeQuery();
            while (resultSet.next()){
                experienceLevelList.add(createExperienceLevelObject(resultSet));
            }
            return experienceLevelList;
        }catch (SQLException ex){
            ex.printStackTrace();
            throw new DaoException("Can not create Levels List");
        }
    }
    @Override
    public boolean changeExperienceLvl(List<Integer> values) throws DaoException{
        try (Connection connection = DBCPDataSource.getConnection()){
            for (int i = 0; i<values.size(); i++){

                PreparedStatement statement = connection.prepareStatement(
                        "UPDATE levels SET level_start = ? WHERE id = ?;");
                statement.setInt(1, values.get(i));
                statement.setInt(2, i+2);
                statement.execute();
            }
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private ExperienceLevel createExperienceLevelObject(ResultSet resultSet) throws SQLException{
        return new ExperienceLevel(resultSet.getInt("id"), resultSet.getString("name")
                , resultSet.getInt("level_start"));
    }
}
