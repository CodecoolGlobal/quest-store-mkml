package com.queststore.Controller;

import com.queststore.DAO.CardDAO;
import com.queststore.DAO.CardDAOSql;
import com.queststore.DAO.DaoException;
import com.queststore.DAO.UserDAOSql;
import com.queststore.Model.Card;
import com.queststore.Model.User;
import com.queststore.helpers.CookieHelper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MentorQuests implements HttpHandler {
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {


        Session session = new Session(new CookieHelper(), new UserDAOSql());
        try {
            Optional<User> user = session.getUserOfSession(httpExchange);
            if (user.isPresent() && session.isUserOfType("mentor", user.get())) {
                sendContent(httpExchange);
            } else {
                session.sendLoginForm(httpExchange);
            }
        } catch (DaoException e) {
            e.printStackTrace();
            send500(httpExchange);
        }

    }

    private void sendContent(HttpExchange httpExchange) throws IOException {

        CardDAO cardDAO = new CardDAOSql();
        List<Card> cardList = new ArrayList<>();
        int questTypeId = 1;

        try {
            cardList.addAll(cardDAO.getCardsOfType(cardDAO.getCardTypeById(questTypeId)));
        } catch (DaoException e) {
            e.printStackTrace();
        }


        JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/mentor-quests.twig");
        JtwigModel model = JtwigModel.newModel();

        model.with("cardsList", cardList);

        String response = template.render(model);

        httpExchange.sendResponseHeaders(200, response.length());
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes());
        os.close();

    }

    private void send500(HttpExchange httpExchange) throws IOException {
        String response = "500 Server internal error\n";
        httpExchange.sendResponseHeaders(500, response.length());
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}

