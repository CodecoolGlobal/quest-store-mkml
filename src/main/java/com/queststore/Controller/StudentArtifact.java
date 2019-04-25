package com.queststore.Controller;

import com.queststore.DAO.DaoException;
import com.queststore.DAO.TransactionDAO;
import com.queststore.Model.Card;
import com.queststore.Model.Transaction;
import com.queststore.Model.User;
import org.jtwig.JtwigModel;

import java.util.ArrayList;
import java.util.List;

public class StudentArtifact extends Student {

    private TransactionDAO transactionDAO;

    public StudentArtifact(TransactionDAO transactionDAO) {
        this.transactionDAO = transactionDAO;
    }

    @Override
    String getTemplatePath() {
        return "/templates/student/student.twig";
    }

    @Override
    JtwigModel getModelWithBodyFilled(User user) {
        List<Card> artifacts = new ArrayList<>();
        try {
            List<Transaction> transactions = transactionDAO.getTransactions(user.getId(), 2);
            for (Transaction transaction : transactions) {
                artifacts.add(transaction.getCard());
            }
        } catch (DaoException e) {
            e.printStackTrace();
        }

        JtwigModel model = JtwigModel.newModel();
        model.with("artifactList", artifacts);

        return model;
    }
}
