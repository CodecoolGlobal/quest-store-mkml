package com.queststore.Controller;


import com.queststore.DAO.DaoException;
import com.queststore.DAO.LoginDao;
import com.queststore.DAO.UserDAO;
import com.queststore.Model.LoginUser;
import com.queststore.Model.User;
import com.queststore.helpers.CookieHelper;
import com.queststore.helpers.HttpUtils;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpCookie;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class Login implements HttpHandler {

    private LoginDao loginDao;
    private CookieHelper cookieHelper;
    private UserDAO userDAO;
    private Session session;

    public Login(LoginDao loginDao, CookieHelper cookieHelper, UserDAO userDAO) {
        this.loginDao = loginDao;
        this.cookieHelper = cookieHelper;
        this.userDAO = userDAO;
        this.session = new Session(cookieHelper, userDAO);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        if (exchange.getRequestMethod().equals("GET")) {
            try {
                Optional<User> user = session.getUserOfSession(exchange);
                if (user.isPresent()) {
                    redirectToMainPage(exchange, user.get());
                } else {
                    session.sendLoginForm(exchange);
                }
            } catch (DaoException e) {
                e.printStackTrace();
                HttpUtils.send500(exchange);
            }

        }

        if (exchange.getRequestMethod().equals("POST")) {
            try {
                LoginUser loginUser = getUser(exchange);
                Optional<Integer> userId = loginDao.getUserId(loginUser);
                if (userId.isPresent()) {
                    Optional<User> user = userDAO.getUserById(userId.get());
                    if (user.isPresent()) {
                        HttpCookie cookie = cookieHelper.generateNewSessionIdCookie();
                        loginDao.saveSession(cookie, userId.get());
                        redirectToMainPage(exchange, user.get(), cookie);
                    } else {
                        session.sendLoginForm(exchange);
                    }
                } else {
                    session.sendLoginForm(exchange);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private LoginUser getUser(HttpExchange exchange) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(exchange.getRequestBody()));
        String userCredentials = br.readLine();
        return createUser(userCredentials);
    }

    private LoginUser createUser(String rawData) throws Exception {
        String[] entries = rawData.split("&");
        String name = URLDecoder.decode(entries[0].split("=")[1].trim(), StandardCharsets.UTF_8.toString());
        String password = URLDecoder.decode(entries[1].split("=")[1].trim(), StandardCharsets.UTF_8.toString());
        System.out.println("Password: " + password);
        return new LoginUser(null, name, password);
    }


    private void redirectToMainPage(HttpExchange exchange, User user) throws IOException {
        Headers headers = exchange.getResponseHeaders();
        switch (user.getUserType().getName()) {
            case "mentor":
                headers.set("Location", "/mentor");
                break;
            case "student":
                headers.set("Location", "/student");
                break;
            case "admin":
                headers.set("Location", "/student");
                break;
            default:
                headers.set("Location", "/login");
        }
        exchange.sendResponseHeaders(303, 0);
    }

    private void redirectToMainPage(HttpExchange exchange, User user, HttpCookie cookie) throws IOException {
        exchange.getResponseHeaders().set("Set-Cookie", cookie.toString());
        redirectToMainPage(exchange, user);
    }

}
