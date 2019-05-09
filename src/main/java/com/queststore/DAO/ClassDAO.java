package com.queststore.DAO;

import com.queststore.Model.Class;
import com.queststore.Model.ClassInfo;

import java.util.List;
import java.util.Optional;

public interface ClassDAO {

    List<Class> getAllClasses() throws DaoException;

    List<Class> getAllClasses(int mentorId) throws DaoException;

    void add(String name) throws DaoException;

    void update(Class cls) throws DaoException;

    void delete(int classId) throws DaoException;
  
    Class createClassFromId(int id) throws DaoException;

    int getMentorsCountByClassId(int id) throws DaoException;

    int getStudentsCountByClassId(int id) throws DaoException;

    Optional<Integer> getClassId(String className) throws  DaoException;

    List<ClassInfo> getClassesInfo() throws DaoException;
}
