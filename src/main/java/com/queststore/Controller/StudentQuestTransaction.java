package com.queststore.Controller;

import com.queststore.DAO.DaoException;
import com.queststore.Model.Card;
import com.queststore.Model.User;

public class StudentQuestTransaction extends StudentTransaction {

    @Override
    boolean canBeBought(Card card, User user) throws DaoException {
        return true;
    }
}
