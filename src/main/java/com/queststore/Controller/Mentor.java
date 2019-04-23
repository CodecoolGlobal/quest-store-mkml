package com.queststore.Controller;

import com.queststore.DAO.DaoException;
import com.queststore.DAO.UserDAO;
import com.queststore.DAO.UserDAOSql;
import com.queststore.Model.User;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

public class Mentor implements HttpHandler {
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        //Get Mentor id from session
        int mentorId = 3;

        UserDAO userDAO = new UserDAOSql();
        List<User> userList = new ArrayList<>();
        try {
            int mentorClass = userDAO.getUserById(mentorId).getUserClass().getId();
            userList.addAll(userDAO.getStudentsFrom(mentorClass));
        } catch (DaoException e) {
            e.printStackTrace();
        }

        JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/mentor.twig");
        JtwigModel model = JtwigModel.newModel();

        model.with("studentsList", userList);
        // render a template to a string
        String response = template.render(model);

        // send the results to a the client
        httpExchange.sendResponseHeaders(200, response.length());
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes());
        os.close();

    }
}
