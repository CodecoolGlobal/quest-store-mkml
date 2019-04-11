package com.queststore.Services;

import com.queststore.DAO.*;
import com.queststore.Model.Class;
import com.queststore.Model.User;

import java.util.ArrayList;
import java.util.List;

public class UserService {

    private UserDAO userDAO;
    private ClassDAO classDAO;

    public UserService(UserDAO userDAO, ClassDAO classDAO) {
        this.userDAO = userDAO;
        this.classDAO = classDAO;
    }

    public static void main(String[] args) {
        UserService userService = new UserService(new UserDAOSql(), new ClassDAOSql());
        try {
            for (User u : userService.getAllStudentsInMentorClass(1)) {
                System.out.println(u.getFirstName());
            }
        } catch (DaoException e) {
            e.printStackTrace();
        }

    }

    List<User> getAllStudentsInMentorClass(int mentorId) throws DaoException {
        List<User> students = new ArrayList<>();
        List<Class> cls = classDAO.getAllClasses(mentorId);
        for (Class c : cls) {
            students.addAll(userDAO.getStudentsFrom(c.getId()));
        }
        return students;
    }



}
