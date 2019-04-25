package com.queststore.Services;

import com.queststore.DAO.CardDAO;
import com.queststore.DAO.CardDAOSql;
import com.queststore.DAO.DaoException;
import com.queststore.Model.Card;
import com.queststore.Model.CardTypes;
import com.queststore.Model.Categories;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class ItemCardUpdate implements HttpHandler {

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        JSONparser jsonParser = new JSONparser();
        String method = httpExchange.getRequestMethod();
        List<String> items = new ArrayList<>();
        List<Card> cardList = new ArrayList<>();
        if(method.equals("POST")) {
            items = jsonParser.parseJSONlistToArray(jsonParser.convertJSONtoString(httpExchange));
        }

        try {
            cardList = updateCardInDB(items);

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


    private List<Card> updateCardInDB(List<String> items) throws DaoException {
        CardDAO cardDAO = new CardDAOSql();
        List<Card> cardList = new ArrayList<>();
        int artifactTypeId = 2;

        Card createCart = new Card(Integer.parseInt(items.get(0)), items.get(1), items.get(3), new Categories(1, "easy"), null,
                Integer.parseInt(items.get(2)), new CardTypes(2, "artifact"), true);
        cardDAO.update(createCart);
        cardList.addAll(cardDAO.getCardsOfType(cardDAO.getCardTypeById(artifactTypeId)));
        return cardList;

    }

}
