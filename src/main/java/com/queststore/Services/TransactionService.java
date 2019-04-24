package com.queststore.Services;

import com.queststore.DAO.*;
import com.queststore.Model.Card;
import com.queststore.Model.Transaction;
import com.queststore.Model.TransactionStatus;
import com.queststore.Model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class TransactionService {
    private CardDAO cardDAO = new CardDAOSql();
    private UserDAO userDAOSql = new UserDAOSql();

    public TransactionService() {

    }

    public Transaction createTransactionObject(ResultSet rs, TransactionStatus transactionStatus) throws DaoException {
        try {
            Optional<User> user = userDAOSql.getUserById(rs.getInt("user_id"));
            //todo change logic chain to use Optional
            Card card = cardDAO.getCardById(rs.getInt("card_id"));
            return new Transaction(rs.getInt("id"), rs.getDate("date"), user.get(), card
            , transactionStatus, rs.getInt("cost"));

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException("TransactionServices can't create transaction");
        }
    }
}
