package com.queststore.Controller;

import com.queststore.DAO.*;
import com.queststore.Model.User;
import com.queststore.helpers.CookieHelper;
import com.queststore.helpers.HttpUtils;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Admin implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Session session = new Session(new CookieHelper(), new UserDAOSql());

        try {
            Optional<User> user = session.getUserOfSession(exchange);
            if (user.isPresent() && session.isUserOfType("admin", user.get())) {
                sendContent(exchange, user.get().getId());
            } else {
                session.sendLoginForm(exchange);
            }
        } catch (DaoException e) {
            e.printStackTrace();
            HttpUtils.send500(exchange);
        }
    }

    private void sendContent(HttpExchange httpExchange, int userId) throws IOException {
        UserDAO userDAO = new UserDAOSql();
        ClassDAO classDAO = new ClassDAOSql();
        List<User> userList = new ArrayList<>();
        List<Object> classesNames = new ArrayList<>();
        try {
            userList.addAll(userDAO.getAllUser("mentor"));
            classesNames.addAll(classDAO.getAllClasses());
        } catch (DaoException e) {
            e.printStackTrace();
        }

        JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/admin/admin.twig");
        JtwigModel model = JtwigModel.newModel();

        model.with("mentorsList", userList);
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
