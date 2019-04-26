package com.queststore.Controller;

import com.queststore.DAO.*;
import com.queststore.Model.User;
import com.queststore.helpers.CookieHelper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONArray;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class Mentor implements HttpHandler {
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        Session session = new Session(new CookieHelper(), new UserDAOSql());

        try {
            Optional<User> user = session.getUserOfSession(httpExchange);
            if (user.isPresent() && session.isUserOfType("mentor", user.get())) {
                sendContent(httpExchange, user.get().getId());
            } else {
                session.sendLoginForm(httpExchange);
            }
        } catch (DaoException e) {
            e.printStackTrace();
            send500(httpExchange);
        }


    }
    private void sendContent(HttpExchange httpExchange, int mentorId) throws IOException {
        UserDAO userDAO = new UserDAOSql();
        ClassDAO classDAO = new ClassDAOSql();
        List<User> userList = new ArrayList<>();
        List<Object> classesNames = new ArrayList<>();
        try {
            int mentorClass = userDAO.getUserById(mentorId).get().getUserClass().getId();
            userList.addAll(userDAO.getStudentsFrom(mentorClass));
            classesNames.addAll(classDAO.getAllClasses());
        } catch (DaoException e) {
            e.printStackTrace();
        }

        JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/mentor.twig");
        JtwigModel model = JtwigModel.newModel();

        model.with("studentsList", userList);
        model.with("classList", classesNames);
        // render a template to a string
        String response = template.render(model);

        // send the results to a the client
        httpExchange.sendResponseHeaders(200, response.length());
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes());
        os.close();

    }

    private void send500(HttpExchange httpExchange) throws IOException {
        String response = "500 Server internal error\n";
        httpExchange.sendResponseHeaders(500, response.length());
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.toString().getBytes());
        os.close();
    }
}
