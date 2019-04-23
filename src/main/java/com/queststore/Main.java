package com.queststore;

import com.queststore.Controller.Mentor;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws IOException {

        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);

        // set routes
        server.createContext("/mentor", new Mentor());
        server.createContext("/static", new Static());


        server.setExecutor(null); // creates a default executor

        // start listening
        server.start();


//        CreateTables createTables = new CreateTables();
//        try {
//            createTables.createAllTables();
//        }catch (SQLException ex){
//            ex.printStackTrace();
//        }
//        CardDAO cardDAOSql = new CardDAOSql();
////        System.out.println(cardDAOSql.getCardById(1).getCategories().getName());

    }
}
