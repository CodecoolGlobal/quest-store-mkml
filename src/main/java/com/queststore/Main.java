package com.queststore;

import com.queststore.Controller.Login;
import com.queststore.Controller.Logout;
import com.queststore.Services.ItemCardAdd;
import com.queststore.Services.ItemCardUpdate;

import com.queststore.Controller.Mentor;
import com.queststore.Controller.MentorItems;
import com.queststore.DAO.LoginDao;
import com.queststore.DAO.UserDAOSql;
import com.queststore.Services.UserCardAdd;
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

        server.createContext("/mentor-students-addupdate", new UserCardUpdate());
        server.createContext("/mentor-students-add", new UserCardAdd());

        LoginDao dao = new LoginDao();
        CookieHelper cookieHelper = new CookieHelper();
        server.createContext("/login", new Login(dao, cookieHelper, new UserDAOSql()));
        server.createContext("/logout", new Logout(dao, cookieHelper));


        server.setExecutor(null); // creates a default executor

        // start listening
        server.start();


    }
}
