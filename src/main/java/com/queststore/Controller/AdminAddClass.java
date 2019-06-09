package com.queststore.Controller;

import com.queststore.DAO.*;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Scanner;

public class AdminAddClass implements HttpHandler {

    ClassDAO classDAO = new ClassDAOSql();

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        if (httpExchange.getRequestMethod().equals("POST")){
            JSONObject jsonObject = parseJSON(httpExchange);
            String className = jsonObject.getString("name");

            try {
                addClassToDB(className);
            } catch (DaoException e) {
                e.printStackTrace();
            }
        }

    }

    private void addClassToDB(String className) throws DaoException {
        classDAO.add(className);
    }

    private JSONObject parseJSON(HttpExchange httpExchange) {

        Scanner scanner = new Scanner(httpExchange.getRequestBody());
        return new JSONObject(scanner.nextLine());
    }
}
