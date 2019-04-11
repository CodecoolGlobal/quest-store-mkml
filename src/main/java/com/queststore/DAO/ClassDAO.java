package com.queststore.DAO;

import com.queststore.Model.Class;

import java.util.List;

public interface ClassDAO {

    List<Class> getAllClasses() throws DaoException;

    List<Class> getAllClasses(int mentorId) throws DaoException;

    void add(String name) throws DaoException;

    void update(Class cls) throws DaoException;

    void delete(int classId) throws DaoException;
  
    Class createClassFromId(int id) throws DaoException;

}
