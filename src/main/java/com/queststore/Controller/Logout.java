package com.queststore.Controller;


import com.queststore.DAO.DaoException;
import com.queststore.DAO.LoginDao;
import com.queststore.helpers.CookieHelper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.net.HttpCookie;
import java.util.Optional;

public class Logout implements HttpHandler {

    private LoginDao loginDao;
    private CookieHelper cookieHelper;

    public Logout(LoginDao loginDao, CookieHelper cookieHelper) {
        this.loginDao = loginDao;
        this.cookieHelper = cookieHelper;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        System.out.println("hello from the other side");
        deleteSessionCookie(exchange);
        exchange.getResponseHeaders().set("Location", "/login");
        exchange.sendResponseHeaders(303, 0);
    }

    private void deleteSessionCookie(HttpExchange exchange) {
        Optional<HttpCookie> httpCookie = cookieHelper.getSessionIdCookie(exchange);
        try {
            if (httpCookie.isPresent()) {
                loginDao.deleteSessionCookie(httpCookie.get());
            }
        } catch (DaoException e) {
            e.printStackTrace();
        }
    }
}
