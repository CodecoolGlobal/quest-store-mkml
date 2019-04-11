package com.queststore.DAO;

import com.queststore.Model.Card;
import com.queststore.Model.Transaction;
import com.queststore.Model.TransactionStatus;
import com.queststore.Model.User;
import com.queststore.Services.TransactionService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAOSql implements TransactionDAO {

    private TransactionService transactionService = new TransactionService();

    public TransactionDAOSql() {

    }

    @Override
    public List<Transaction> getTransactions(int userId, int type) throws DaoException {
        String SQL = "SELECT * FROM transactions LEFT JOIN cards ON transactions.card_id = cards.id " +
                "WHERE user_id = ? AND cards.card_type_id = ?";
        List<Transaction> transactionList = new ArrayList<>();
        try (Connection connection = DBCPDataSource.getConnection()){
            PreparedStatement pstmt = connection.prepareStatement(SQL);
            pstmt.setInt(1, userId);
            pstmt.setInt(2, type);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {

                transactionList.add(transactionService.createTransactionObject(rs, getTransactionStatusById(rs.getInt("status_id"))));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException("An error occured during get transactions list");
        }
        return transactionList;
    }


    public TransactionStatus getTransactionStatusById(int id) throws DaoException{
        String SQL = "SELECT name FROM transactions_status WHERE id=?";
        TransactionStatus transactionStatus = null;
        try(Connection connection = DBCPDataSource.getConnection()){
            PreparedStatement pstmt = connection.prepareStatement(SQL);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            rs.next();
            transactionStatus = new TransactionStatus(id, rs.getString("name"));
        }catch (SQLException ex){
            ex.printStackTrace();
            throw new DaoException("An error occured during get transaction status");
        }
        return transactionStatus;
    }


}
