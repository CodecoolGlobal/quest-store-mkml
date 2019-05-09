package com.queststore.Controller;

import com.queststore.DAO.*;
import com.queststore.Model.ClassInfo;
import com.queststore.Model.ExperienceLevel;
import com.queststore.Model.User;
import com.queststore.helpers.CookieHelper;
import com.queststore.helpers.HttpUtils;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Optional;

public class AdminConfiguration implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Session session = new Session(new CookieHelper(), new UserDAOSql());

        try {
            Optional<User> user = session.getUserOfSession(exchange);
            if (user.isPresent() && session.isUserOfType("admin", user.get())) {
                sendContent(exchange, user.get().getId());
            } else {
                session.sendLoginForm(exchange);
            }
        } catch (DaoException e) {
            e.printStackTrace();
            HttpUtils.send500(exchange);
        }
    }

    private void sendContent(HttpExchange httpExchange, int userId) throws IOException, DaoException {

        ClassDAO classDAO = new ClassDAOSql();
        ConfigurationDAO configurationDAO = new ConfigurationDAOSql();
        List<ClassInfo> classesInfo = classDAO.getClassesInfo();
        List<ExperienceLevel> experienceLevels = configurationDAO.getAllLevels();

        JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/admin/admin_configuration.twig");
        JtwigModel model = JtwigModel.newModel();

        model.with("experienceLevels", experienceLevels);
        model.with("classesInfo", classesInfo);
        // render a template to a string
        String response = template.render(model);

        // send the results to a the client
        httpExchange.sendResponseHeaders(200, response.length());
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes());
        os.close();

    }
}
