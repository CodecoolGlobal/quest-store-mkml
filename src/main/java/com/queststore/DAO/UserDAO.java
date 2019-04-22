package com.queststore.DAO;

import com.queststore.Model.User;
import com.queststore.Model.UserType;

import java.util.List;
import java.util.Optional;

public interface UserDAO {

    Optional<User> getUser(String email, String password) throws DaoException;

    List<User> getStudentsFrom(int classId) throws DaoException;

    User getUserById(int id) throws DaoException;

    UserType getUserTypeFromId (int id) throws DaoException;

    List<User> getAllMentors() throws DaoException;



    void update(User user) throws DaoException;

    void add(User user, String password) throws DaoException;

    void delete(int userId) throws DaoException;
}
