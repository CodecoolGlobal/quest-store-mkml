package com.queststore.Controller;

import com.queststore.DAO.*;
import com.queststore.Model.User;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONArray;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class Mentor implements HttpHandler {
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        int mentorId = 2;
        //TODO: take mentor id from session

        String method = httpExchange.getRequestMethod();
        if(method.equals("POST")) {
//            parseEditUserFromJSON(httpExchange);
        }

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
}
