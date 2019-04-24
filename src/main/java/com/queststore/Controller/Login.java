package com.queststore.Controller;


import com.queststore.DAO.DaoException;
import com.queststore.DAO.LoginDao;
import com.queststore.DAO.UserDAO;
import com.queststore.Model.LoginUser;
import com.queststore.Model.User;
import com.queststore.helpers.CookieHelper;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.HttpCookie;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class Login implements HttpHandler {

    private LoginDao loginDao;
    private CookieHelper cookieHelper;
    private UserDAO userDAO;

    public Login(LoginDao loginDao, CookieHelper cookieHelper, UserDAO userDAO) {
        this.loginDao = loginDao;
        this.cookieHelper = cookieHelper;
        this.userDAO = userDAO;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        if (exchange.getRequestMethod().equals("GET")) {
            Optional<HttpCookie> cookie = cookieHelper.getSessionIdCookie(exchange);
            if (cookie.isPresent()) {
                try {
                    Optional<User> user = userDAO.getUserOfSession(cookie.get());
                    if (user.isPresent()) {
                        redirectToMainPage(exchange, user.get());
                    } else {
                        sendLoginForm(exchange);
                    }
                } catch (DaoException e) {
                    e.printStackTrace();
                }

            } else {
                sendLoginForm(exchange);
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
                        sendLoginForm(exchange);
                    }
                } else {
                    sendLoginForm(exchange);
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
        String name = URLDecoder.decode(entries[0].split("=")[1].trim(), StandardCharsets.UTF_8);
        String password = URLDecoder.decode(entries[1].split("=")[1].trim(), StandardCharsets.UTF_8);
        System.out.println("Password: " + password);
        return new LoginUser(null, name, password);
    }

    private void redirectToMainPage(HttpExchange exchange, User user) throws IOException {
        Headers headers = exchange.getResponseHeaders();
        switch (user.getUserType().getName()) {
            case "mentor":
                headers.set("Location", "Http://" + exchange.getRequestHeaders().getFirst("Host") + "/mentor");
                break;
            case "student":
                headers.set("Location", "Http://" + exchange.getRequestHeaders().getFirst("Host") + "/student");
                break;
            case "admin":
                headers.set("Location", "Http://" + exchange.getRequestHeaders().getFirst("Host") + "/student");
                break;
            default:
                headers.set("Location", "Http://" + exchange.getRequestHeaders().getFirst("Host") + "/login");
        }
        headers.set("Content-Type", "text/html");
        exchange.sendResponseHeaders(200, 0);
        OutputStream os = exchange.getResponseBody();
        os.write("".getBytes());
        os.close();
    }

    private void redirectToMainPage(HttpExchange exchange, User user, HttpCookie cookie) throws IOException {
        exchange.getResponseHeaders().set("Set-Cookie", cookie.toString());
        redirectToMainPage(exchange, user);
    }

    private void sendLoginForm(HttpExchange exchange) throws IOException {
        File file = new File("login.html");
        exchange.getResponseHeaders().set("Content-Type", "text/html");
        exchange.sendResponseHeaders(200, 0);
        OutputStream os = exchange.getResponseBody();
        FileInputStream fs = new FileInputStream(file);
        final byte[] buffer = new byte[0x10000];
        int count;
        while ((count = fs.read(buffer)) >= 0){
            os.write(buffer,0, count);
        }
        os.close();
    }

}
