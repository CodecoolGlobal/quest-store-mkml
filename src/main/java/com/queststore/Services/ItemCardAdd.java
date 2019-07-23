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

public class ItemCardAdd implements HttpHandler {
    private CardDAO cardDAO;

    ItemCardAdd(CardDAO cardDAO) {
        this.cardDAO = cardDAO;
    }

    public ItemCardAdd() {
        this.cardDAO = new CardDAOSql();
    }

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
            cardList = addCardToDB(items);

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

    public List<Card> addCardToDB(List<String> items) throws DaoException {
        List<Card> cardList = new ArrayList<>();
        int artifactTypeId = 2;
        Card newCard = new Card(4, items.get(0), items.get(2), new Categories(1, "easy"), null,
                Integer.parseInt((String) items.get(1)), new CardTypes(artifactTypeId, "artifact"), true);
        cardDAO.add(newCard);
        cardList.add(newCard);
        return cardList;
    }

}
