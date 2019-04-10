package com.queststore.DAO;

import com.queststore.Model.Transaction;

import java.util.List;

public interface TransactionDAO {

    List<Transaction> getTransactions(int userId, int type) throws DaoException;
}
