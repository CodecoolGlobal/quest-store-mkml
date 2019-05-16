package com.queststore.Services;

import com.queststore.DAO.*;
import com.queststore.Model.Class;
import com.queststore.Model.ExperienceLevel;
import com.queststore.Model.Transaction;
import com.queststore.Model.User;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class UserService {

    private UserDAO userDAO;
    private ClassDAO classDAO;
    private TransactionDAO transactionDAO;
    private ConfigurationDAO configurationDAOsql;

    public UserService(UserDAO userDAO, ClassDAO classDAO, TransactionDAO transactionDAO) {
        this.userDAO = userDAO;
        this.classDAO = classDAO;
        this.transactionDAO = transactionDAO;
        if(configurationDAOsql==null) this.configurationDAOsql = new ConfigurationDAOSql();
    }

    UserService(UserDAO userDAO, ClassDAO classDAO, TransactionDAO transactionDAO, ConfigurationDAO configurationDAO) {
        this(userDAO, classDAO, transactionDAO);
        this.configurationDAOsql = configurationDAO;
    }

    List<User> getAllStudentsInMentorClass(int mentorId) throws DaoException {
        List<User> students = new ArrayList<>();
        List<Class> cls = classDAO.getAllClasses(mentorId);
        for (Class c : cls) {
            students.addAll(userDAO.getStudentsFrom(c.getId()));
        }
        return students;
    }


    public int getCoinBalance(int userId) throws DaoException {
        List<Transaction> questsList = new ArrayList<>();
        List<Transaction> artifactsList = new ArrayList<>();
        int questId = 1;
        int artifactId = 2;

        questsList.addAll(transactionDAO.getTransactions(userId, questId));
        artifactsList.addAll(transactionDAO.getTransactions(userId, artifactId));

        int coinBalance = 0;
        for (Transaction transaction : questsList) {
            if (transaction.getTransactionStatus().getName().equals("accepted")) {
                coinBalance += transaction.getCost();
            }
        }

        for (Transaction transaction : artifactsList) {
            if (transaction.getTransactionStatus().getName().equals("accepted")) {
                coinBalance -= transaction.getCost();
            }
        }

        return coinBalance;
    }

    public String calculateUserLvl(int userId) throws DaoException {
        List<Transaction> questsList = new ArrayList<>();
        int questId = 1;
        questsList.addAll(transactionDAO.getTransactions(userId, questId));

        int coinBalance = 0;
        for (Transaction transaction : questsList) {
            if (transaction.getTransactionStatus().getName().equals("accepted")) {
                coinBalance += transaction.getCost();
            }
        }

        int balance = coinBalance;

        List<ExperienceLevel> experienceLevelList = configurationDAOsql.getAllLevels();
        experienceLevelList.sort(Comparator.comparing(ExperienceLevel::getName));

        Optional<ExperienceLevel> level = experienceLevelList.stream()
                .filter(experienceLevel -> experienceLevel.getLevelStart() <= balance)
                .max((Comparator.comparingInt(ExperienceLevel::getLevelStart)));

        return level.isPresent() ? level.get().getName() : "noname";

    }

}
