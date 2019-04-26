package com.queststore.Controller;

import com.queststore.DAO.DaoException;
import com.queststore.DAO.UserDAO;
import com.queststore.Model.User;
import com.queststore.helpers.CookieHelper;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.net.HttpCookie;
import java.util.Optional;

class Session {

    private CookieHelper cookieHelper;
    private UserDAO userDAO;

    Session(CookieHelper cookieHelper, UserDAO userDAO) {
        this.cookieHelper = cookieHelper;
        this.userDAO = userDAO;
    }

    Optional<User> getUserOfSession(HttpExchange exchange) throws DaoException {
        Optional<HttpCookie> cookie = cookieHelper.getSessionIdCookie(exchange);
        if (cookie.isPresent()) {
            return userDAO.getUserOfSession(cookie.get());
        } else {
            return Optional.empty();
        }
    }

    boolean isUserOfType(String type, User user) {
        return user.getUserType().getName().equals(type);
    }



    void sendLoginForm(HttpExchange exchange) throws IOException {
        exchange.getResponseHeaders().set("Location", "/static/login.html");
        exchange.sendResponseHeaders(303, 0);
    }
}
