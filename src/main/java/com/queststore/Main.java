package com.queststore;

import com.queststore.Controller.*;
import com.queststore.DAO.CardDAOSql;
import com.queststore.Services.CardService;
import com.queststore.Services.ItemCardAdd;
import com.queststore.Services.ItemCardUpdate;
import com.queststore.DAO.LoginDao;
import com.queststore.DAO.UserDAOSql;
import com.queststore.Services.UserCardUpdate;
import com.queststore.helpers.CookieHelper;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Main {
    public static void main(String[] args) throws IOException {

        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);

        // set routes
        server.createContext("/mentor", new Mentor());
        server.createContext("/mentor-items", new MentorItems());
        server.createContext("/static", new Static());
        server.createContext("/mentor-add-items", new ItemCardAdd());
        server.createContext("/mentor-update-items", new ItemCardUpdate());
        server.createContext("/user-update", new UserCardUpdate());

        LoginDao dao = new LoginDao();
        CookieHelper cookieHelper = new CookieHelper();
        CardService cardService = new CardService(new CardDAOSql());
        server.createContext("/login", new Login(dao, cookieHelper, new UserDAOSql()));
        server.createContext("/logout", new Logout(dao, cookieHelper));
        server.createContext("/student", new StudentArtifact(cardService));
        server.createContext("/student-item-store", new StudentArtifactStore(cardService));
        server.createContext("/student-quest-store", new StudentQuestStore(cardService));

        server.createContext("/buy-artifact", new StudentArtifactTransaction());
        server.createContext("/claim-quest", new StudentQuestTransaction());


        server.setExecutor(null); // creates a default executor

        // start listening
        server.start();


    }
}
