package com.queststore.Controller;

import com.queststore.DAO.ClassDAOSql;
import com.queststore.DAO.DaoException;
import com.queststore.DAO.TransactionDAOSql;
import com.queststore.DAO.UserDAOSql;
import com.queststore.Model.Card;
import com.queststore.Model.User;
import com.queststore.Services.UserService;

public class StudentArtifactTransaction extends StudentTransaction {

    private UserService userService = new UserService(new UserDAOSql(), new ClassDAOSql(), new TransactionDAOSql());

    boolean canBeBought(Card card, User user) throws DaoException {
        int balance = userService.getCoinBalance(user.getId());
        return balance >= card.getValue();
    }

}
