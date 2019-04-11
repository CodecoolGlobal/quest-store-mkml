package com.queststore.DAO;

import com.queststore.Model.User;

import java.util.List;
import java.util.Optional;

public interface UserDAO {

    Optional<User> getUser(String email, String password) throws DaoException;

    List<User> getStudentsFrom(int classId) throws DaoException;

    User getUserById(int id) throws DaoException;
}
