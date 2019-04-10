package com.queststore.Services;

import com.queststore.DAO.DaoException;
import com.queststore.DAO.TransactionDAO;
import com.queststore.DAO.TransactionDAOSql;
import com.queststore.Model.Transaction;
import com.queststore.Model.TransactionStatus;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TransactionService {
    private TransactionDAO transactionDAO;

    public TransactionService(TransactionDAO transactionDAO) {
        this.transactionDAO = transactionDAO;
    }

    public Transaction createTransactionObject(ResultSet rs) throws SQLException {
        TransactionStatus transactionStatus = null;
        while(rs.next()){
            try {
                transactionStatus = transactionDAO.getTransactionStatusById(rs.getInt("id"));


            } catch (DaoException e) {
                e.printStackTrace();
            }
        }


        return null;
    }
}
