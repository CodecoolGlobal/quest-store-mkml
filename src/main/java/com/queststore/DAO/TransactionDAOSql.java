package com.queststore.DAO;

import com.queststore.Model.Transaction;

import java.util.List;

public class TransactionDAOSql implements TransactionDAO {


    @Override
    public List<Transaction> getTransactions(int userId, int type) throws DaoException {
        String SQL = "SELECT * FROM transactions WHERE user_id = ? AND ";
        return null;
    }
}
