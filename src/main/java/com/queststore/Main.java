package com.queststore;

import com.queststore.Controller.ItemCardAdd;
import com.queststore.Controller.ItemCardUpdate;
import com.queststore.Controller.Mentor;
import com.queststore.Controller.MentorItems;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws IOException {

        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);

        // set routes
        server.createContext("/mentor", new Mentor());
        server.createContext("/mentor-items", new MentorItems());
        server.createContext("/static", new Static());
        server.createContext("/mentor-add-items", new ItemCardAdd());
        server.createContext("/mentor-update-items", new ItemCardUpdate());


        server.setExecutor(null); // creates a default executor

        // start listening
        server.start();


    }
}
