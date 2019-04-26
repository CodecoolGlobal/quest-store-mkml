package com.queststore.Controller;

import com.queststore.DAO.*;
import com.queststore.Model.Transaction;
import com.queststore.Model.User;
import com.queststore.helpers.CookieHelper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MentorAchievedQuest implements HttpHandler {
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        Session session = new Session(new CookieHelper(), new UserDAOSql());
        try {
            Optional<User> user = session.getUserOfSession(httpExchange);
            if (user.isPresent() && session.isUserOfType("mentor", user.get())) {
                sendContent(httpExchange);
            } else {
                session.sendLoginForm(httpExchange);
            }
        } catch (DaoException e) {
            e.printStackTrace();
            send500(httpExchange);
        }

    }

    private void sendContent(HttpExchange httpExchange) throws IOException {

        TransactionDAO transactionDAO = new TransactionDAOSql();
        List<Transaction> transactionList = new ArrayList<>();
        int statusId = 1;

    }
    private void send500(HttpExchange httpExchange) throws IOException {
        String response = "500 Server internal error\n";
        httpExchange.sendResponseHeaders(500, response.length());
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.toString().getBytes());
        os.close();
    }
}
