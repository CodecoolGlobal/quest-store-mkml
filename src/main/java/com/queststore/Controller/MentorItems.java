package com.queststore.Controller;

import com.queststore.DAO.*;
import com.queststore.Model.Card;
import com.queststore.Model.CardTypes;
import com.queststore.Model.Categories;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

public class MentorItems implements HttpHandler {
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        CardDAO cardDAO = new CardDAOSql();
        List<Card> cardList = new ArrayList<>();
        int artifactTypeId = 2;

        try {
            cardList.addAll(cardDAO.getCardsOfType(cardDAO.getCardTypeById(artifactTypeId)));
        } catch (DaoException e) {
            e.printStackTrace();
        }


        JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/mentor-items.twig");
        JtwigModel model = JtwigModel.newModel();

        model.with("cardsList", cardList);

        // render a template to a string
        String response = template.render(model);

        // send the results to a the client
        httpExchange.sendResponseHeaders(200, response.length());
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes());
        os.close();

    }


}
