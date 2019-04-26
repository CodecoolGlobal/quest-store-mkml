package com.queststore.Controller;

import com.queststore.DAO.ClassDAOSql;
import com.queststore.DAO.DaoException;
import com.queststore.DAO.TransactionDAOSql;
import com.queststore.DAO.UserDAOSql;
import com.queststore.Model.User;
import com.queststore.Services.UserService;
import com.queststore.helpers.CookieHelper;
import com.queststore.helpers.HttpUtils;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Optional;

public abstract class Student implements HttpHandler {

    private UserService userService = new UserService(new UserDAOSql(), new ClassDAOSql(), new TransactionDAOSql());

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Session session = new Session(new CookieHelper(), new UserDAOSql());

        try {
            Optional<User> user = session.getUserOfSession(exchange);
            if (user.isPresent() && session.isUserOfType("student", user.get())) {
                sendContent(exchange, user.get());
            } else {
                session.sendLoginForm(exchange);
            }
        }catch (DaoException e) {
            e.printStackTrace();
            HttpUtils.send500(exchange);
        }

    }

    private void sendContent(HttpExchange exchange, User user) throws IOException, DaoException {
        String page = getRenderedPage(user);
        exchange.sendResponseHeaders(200, page.length());
        OutputStream os = exchange.getResponseBody();
        os.write(page.getBytes());
        os.close();
    }

    private String getRenderedPage(User user) throws DaoException {

        JtwigTemplate template = JtwigTemplate.classpathTemplate(getTemplatePath());
        JtwigModel model = getModelWithBodyFilled(user);

        setUserWallet(model, user);

        return template.render(model);
    }

    abstract String getTemplatePath();

    abstract JtwigModel getModelWithBodyFilled(User user);

    private void setUserWallet(JtwigModel model, User user) throws DaoException {
        model.with("coins", userService.getCoinBalance(user.getId()));
        model.with("level", userService.calculateUserLvl(user.getId()));
    }
}
