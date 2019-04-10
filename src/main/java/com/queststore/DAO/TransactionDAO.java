package com.queststore.DAO;

import com.queststore.Model.Transaction;
import com.queststore.Model.TransactionStatus;

import java.util.List;

public interface TransactionDAO {

    List<Transaction> getTransactions(int userId, int type) throws DaoException;

    TransactionStatus getTransactionStatusById(int id) throws DaoException;


}
