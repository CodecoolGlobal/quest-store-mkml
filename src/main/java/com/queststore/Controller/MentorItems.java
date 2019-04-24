package com.queststore.Controller;

import com.queststore.DAO.*;
import com.queststore.Model.Card;
import com.queststore.Model.CardTypes;
import com.queststore.Model.Categories;
import com.queststore.Model.User;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.*;

import jdk.nashorn.api.scripting.JSObject;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

public class MentorItems implements HttpHandler {
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        //try take POST from JS
        String formData="";
        String method = httpExchange.getRequestMethod();
        List<String> items = new ArrayList<>();
        if(method.equals("POST")) {
            InputStreamReader isr = new InputStreamReader(httpExchange.getRequestBody(), "utf-8");
            BufferedReader br = new BufferedReader(isr);
            formData = br.readLine();
            formData = formData.replace("\"", "");
            formData = formData.replace("[", "");
            formData = formData.replace("]", "");

            System.out.println(formData);
            items = Arrays.asList(formData.split("[,]"));

        }

//        System.out.println(formData);

        CardDAO cardDAO = new CardDAOSql();
        List<Card> cardList = new ArrayList<>();
        int artifactTypeId = 2;
//        System.out.println(newCard.getDescription());
        try {
            if(!items.isEmpty() & items.size()==3){
                Card newCard = new Card(4, items.get(0), items.get(2), new Categories(1, "easy"), null,
                        Integer.parseInt(items.get(1)), new CardTypes(2, "artifact"), true);
            cardDAO.add(newCard);
            } else if(!items.isEmpty() & items.size()==4){
                Card createCart = new Card(Integer.parseInt(items.get(0)), items.get(1), items.get(3), new Categories(1, "easy"), null,
                        Integer.parseInt(items.get(2)), new CardTypes(2, "artifact"), true);
                cardDAO.update(createCart);
            }
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
