package com.queststore.Services;

import com.queststore.DAO.ClassDAO;
import com.queststore.DAO.DaoException;
import com.queststore.Model.Class;

public class ClassService {

    ClassDAO classDAO;

    public ClassService(ClassDAO classDAO) {
        this.classDAO = classDAO;
    }

    public void addNewClass(String name) throws DaoException {
        classDAO.add(name);
    }

    public void editClass(Class cls) throws DaoException {
        classDAO.update(cls);
    }

    public void deleteClass(int classId) throws DaoException {
        classDAO.delete(classId);
    }
}
