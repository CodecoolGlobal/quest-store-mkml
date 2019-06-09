package com.queststore.DAO;

import com.queststore.Model.ExperienceLevel;

import java.util.List;

public interface ConfigurationDAO {

    List<ExperienceLevel> getAllLevels() throws DaoException;
    public boolean changeExperienceLvl(List<Integer> values) throws DaoException;

}
