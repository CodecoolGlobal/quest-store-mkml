package com.queststore.Services;

import com.queststore.DAO.*;
import com.queststore.Model.*;
import com.queststore.Model.Class;

import java.util.ArrayList;
import java.util.List;

public class UserService {

    private UserDAO userDAO;
    private ClassDAO classDAO;
    private TransactionDAO transactionDAO;
    public UserService(UserDAO userDAO, ClassDAO classDAO, TransactionDAO transactionDAO) {
        this.userDAO = userDAO;
        this.classDAO = classDAO;
        this.transactionDAO = transactionDAO;
    }

    public static void main(String[] args) throws DaoException{
        UserService userService = new UserService(new UserDAOSql(), new ClassDAOSql(), new TransactionDAOSql());

//        try {
//            for (User u : userService.getAllStudentsInMentorClass(1)) {
//                System.out.println(u.getFirstName());
//            }
//        } catch (DaoException e) {
//            e.printStackTrace();
//        }

        System.out.println(userService.getCoinBalance(1));

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

        Integer coinBalance = 0;
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


}
