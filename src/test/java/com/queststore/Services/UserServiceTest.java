package com.queststore.Services;

import com.queststore.DAO.*;
import com.queststore.Model.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
//Integer id, Date date, User user, Card card, TransactionStatus transactionStatus, int cost) {
    @Test
    public void calculateUserLvl() throws DaoException {
        User user = new User(100, "kuba", "buba", "kubabuba@kupa.pl", null, null, new UserType(0, "student"));
        TransactionDAO transactionDAOMock = getTransactionDAOMock();
        ConfigurationDAO configurationDAOMock = getConfigurationDAOMock();

        UserService userService = new UserService(
                null,
                null,
                transactionDAOMock,
                configurationDAOMock
        );

        assertEquals("pro", userService.calculateUserLvl(100));
    }

    private TransactionDAO getTransactionDAOMock() throws DaoException {
        TransactionDAO transactionDAOMock = mock(TransactionDAOSql.class);
        UserType userType = new UserType(1, "accepted");
        List<Transaction> transactionList = new ArrayList<>();
        transactionList.add(new Transaction(
                0,
                null,
                null,
                null,
                new TransactionStatus(1, "accepted"),
                120
        ));
        when(transactionDAOMock.getTransactions(100, 1)).thenReturn(transactionList);
        return transactionDAOMock;
    }

    private ConfigurationDAO getConfigurationDAOMock() throws DaoException {
        ConfigurationDAO configurationDAOMock = mock(ConfigurationDAOSql.class);
        List<ExperienceLevel> experienceLevels = getSampleExperienceLevelsList();
        when(configurationDAOMock.getAllLevels()).thenReturn(experienceLevels);
        return configurationDAOMock;
    }

    private List<ExperienceLevel> getSampleExperienceLevelsList() {
        List<ExperienceLevel> experienceLevels = new ArrayList<>();
        experienceLevels.add(new ExperienceLevel(1, "noob0", 0));
        experienceLevels.add(new ExperienceLevel(2, "noob2", 30));
        experienceLevels.add(new ExperienceLevel(3, "noob3", 50));
        experienceLevels.add(new ExperienceLevel(4, "good", 80));
        experienceLevels.add(new ExperienceLevel(5, "pro", 119));
        return experienceLevels;
    }
}