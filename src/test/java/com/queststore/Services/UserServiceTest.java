package com.queststore.Services;

import com.queststore.DAO.ClassDAOSql;
import com.queststore.DAO.DaoException;
import com.queststore.DAO.TransactionDAO;
import com.queststore.DAO.UserDAOSql;
import com.queststore.Model.Transaction;
import com.queststore.Model.TransactionStatus;
import com.queststore.Model.User;
import com.queststore.Model.UserType;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserServiceTest {


    @Test
    public void getCoinBalance() throws DaoException {
        User user = new User(100, "kuba", "buba", "kubabuba@kupa.pl", null, null, new UserType(0, "student"));
        TransactionDAO transactionDAOMock = mock(TransactionDAO.class);

        UserService userService = new UserService(new UserDAOSql(), new ClassDAOSql(), transactionDAOMock);

        TransactionStatus transactionStatus = new TransactionStatus(1, "accepted");
        when(transactionDAOMock.getTransactions(100, 1)).thenReturn(Collections.singletonList(new Transaction(1, null, null, null, transactionStatus, 40)));
        when(transactionDAOMock.getTransactions(100, 2)).thenReturn(Collections.singletonList(new Transaction(1, null, null, null, transactionStatus, 30)));

        assertEquals(10, userService.getCoinBalance(user.getId()));
    }
}