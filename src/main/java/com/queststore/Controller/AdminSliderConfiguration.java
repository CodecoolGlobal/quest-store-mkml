package com.queststore.Controller;

import com.queststore.DAO.ConfigurationDAO;
import com.queststore.DAO.ConfigurationDAOSql;
import com.queststore.DAO.DaoException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class AdminSliderConfiguration implements HttpHandler {

    ConfigurationDAO configurationDAO = new ConfigurationDAOSql();

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        if( httpExchange.getRequestMethod().equals("POST")){
            JSONObject jsonObject = parseJSON(httpExchange);
            List <Integer> sliderValues = new ArrayList<>(Arrays.asList(
                    jsonObject.getInt("0"),
                    jsonObject.getInt("1"),
                    jsonObject.getInt("2")
            ));
            try {
                changeExpDAO(sliderValues);
            } catch (DaoException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean changeExpDAO(List<Integer> sliderValues) throws DaoException {

        return configurationDAO.changeExperienceLvl(sliderValues);
    }

    private JSONObject parseJSON(HttpExchange httpExchange) {
        Scanner scanner = new Scanner(httpExchange.getRequestBody());
        return new JSONObject(scanner.nextLine());    }
}
