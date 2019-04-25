package com.queststore.Controller;

import com.queststore.DAO.*;
import com.queststore.Model.Card;
import com.queststore.Model.CardTypes;
import com.queststore.Model.User;
import com.queststore.Services.CardService;
import com.queststore.Services.UserService;
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

public class StudentQuestStore implements HttpHandler {

    private CardService cardService;
    private TransactionDAO transactionDAO = new TransactionDAOSql();
    private UserService userService = new UserService(new UserDAOSql(), new ClassDAOSql(), new TransactionDAOSql());

    public StudentQuestStore(CardService cardService) {
        this.cardService = cardService;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Session session = new Session(new CookieHelper(), new UserDAOSql());

        try {
            Optional<User> user = session.getUserOfSession(exchange);
            if (user.isPresent() && session.isUserOfType("student", user.get())) {
                sendContent(exchange, user.get());
            } else {
                session.sendLoginForm(exchange);
            }
        }catch (DaoException e) {
            e.printStackTrace();
            send500(exchange);
        }

    }

    private void sendContent(HttpExchange exchange, User user) throws IOException, DaoException {
        String page = getRenderedPage(user);
        exchange.sendResponseHeaders(200, page.length());
        OutputStream os = exchange.getResponseBody();
        os.write(page.getBytes());
        os.close();
    }

    private String getRenderedPage(User user) throws DaoException {

        List<Card> quests = new ArrayList<>();
        try {
            quests.addAll(cardService.getAllCards(new CardTypes(1, "quest")));
        } catch (DaoException e) {
            e.printStackTrace();
        }

        JtwigTemplate template = JtwigTemplate.classpathTemplate("/templates/student/student_quest_store.twig");
        JtwigModel model = JtwigModel.newModel();

        setUserWallet(model, user);
        model.with("questList", quests);

        return template.render(model);
    }

    private void setUserWallet(JtwigModel model, User user) throws DaoException {
        model.with("coins", userService.getCoinBalance(user.getId()));
        model.with("level", userService.calculateUserLvl(user.getId()));
    }

    private void send500(HttpExchange httpExchange) throws IOException {
        String response = "500 Server internal error\n";
        httpExchange.sendResponseHeaders(500, response.length());
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.toString().getBytes());
        os.close();
    }
}
