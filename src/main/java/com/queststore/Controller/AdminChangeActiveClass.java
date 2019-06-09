package com.queststore.Controller;

import com.queststore.DAO.ClassDAO;
import com.queststore.DAO.ClassDAOSql;
import com.queststore.DAO.DaoException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Scanner;

public class AdminChangeActiveClass implements HttpHandler {

    ClassDAO classDAO = new ClassDAOSql();

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        if( httpExchange.getRequestMethod().equals("POST")){
            JSONObject jsonObject = parseJSON(httpExchange);
            String deActivateKey = "deActivateId";
            String activateKey = "activateId";
            try {
            if (jsonObject.keys().next().equals(deActivateKey)){
                Integer deActivateId = jsonObject.getInt(deActivateKey);
                classDAO.delete(deActivateId);

            }else if (jsonObject.keys().next().equals(activateKey)){
                Integer activateId = jsonObject.getInt(activateKey);
                classDAO.activate(activateId);
            }

            } catch (DaoException e) {
                e.printStackTrace();
            }

        }
    }

    private JSONObject parseJSON(HttpExchange httpExchange) {
        Scanner scanner = new Scanner(httpExchange.getRequestBody());
        return new JSONObject(scanner.nextLine());    }
}
