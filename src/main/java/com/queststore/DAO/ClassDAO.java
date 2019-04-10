package com.queststore.DAO;

import com.queststore.Model.Class;

import java.util.List;

public interface ClassDAO {

    List<Class> getAllClasses() throws DaoException;

    List<Class> getAllClasses(int mentorId) throws DaoException;

}
