package com.queststore.DAO;

import com.queststore.Model.User;

import java.util.Optional;

public interface UserDAO {

    Optional<User> getUser(String email, String password) throws DaoException;
}
