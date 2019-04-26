package com.queststore.Controller;

import com.queststore.Controller.Session;
import com.queststore.DAO.*;
import com.queststore.Model.Class;
import com.queststore.Model.User;
import com.queststore.Model.UserType;
import com.queststore.Services.JSONparser;
import com.queststore.helpers.CookieHelper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

import java.io.IOException;
import java.io.OutputStream;
import java.util.*;
public class UserCardUpdate implements HttpHandler {

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
        JSONparser jsonParser = new JSONparser();
        List<String> items = new ArrayList<>();
        List<User> usersList = new ArrayList<>();
        ClassDAO classDAO = new ClassDAOSql();
        List<Object> classesNames = new ArrayList<>();

        String method = httpExchange.getRequestMethod();
        if(method.equals("POST")) {

            try {
                items = jsonParser.parseJSONlistToArray(jsonParser.convertJSONtoString(httpExchange));
            } catch (IOException e) {
                e.printStackTrace();
            }
            String[] splited = items.get(1).split("\\s+");
            items.remove(1);
            if (splited.length == 2) {
                items.addAll(Arrays.asList(splited));
            } else if (splited.length == 1) {
                items.addAll(Arrays.asList(splited));
                items.add("UNDEFINDED");
            }
            try {
                updateUsersInDB(items);
            } catch (DaoException e) {
                e.printStackTrace();
            }
        }

        try {
            usersList = usersList();
            classesNames.addAll(classDAO.getAllClasses());

        } catch (DaoException e) {
            e.printStackTrace();
        }
        String response = "";

        JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/mentor-students.twig");
        JtwigModel model = JtwigModel.newModel();
        model.with("usersList", usersList);
        model.with("classList", classesNames);

        response = template.render(model);


        // send the results to a the client
        httpExchange.sendResponseHeaders(200, response.length());
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
    private void send500(HttpExchange httpExchange) throws IOException {
        String response = "500 Server internal error\n";
        httpExchange.sendResponseHeaders(500, response.length());
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.toString().getBytes());
        os.close();
    }
    private void updateUsersInDB(List<String> items) throws DaoException {
        UserDAO userDAO = new UserDAOSql();

        User createNewUser = new User.UserBuilder()
                .id(Integer.parseInt(items.get(0)))
                .firstName(items.get(items.size()-2))
                .lastName(items.get(items.size()-1))
                .email(items.get(1))
                .userClass(new Class(Integer.parseInt(items.get(2)), null))
                .userType(userDAO.getUserTypeFromId(Integer.parseInt(items.get(items.size()-3))))
                .createUser();

        userDAO.update(createNewUser);

    }
    private List<User> usersList() throws DaoException {
        UserDAO userDAO = new UserDAOSql();
        List<User> userList = new ArrayList<>();
        userList.addAll(userDAO.getAllUser("student"));
        return userList;
    }
    private List<User> getAllMentorStudents(int classId){
        //TODO: check mentor classid and take all students with this classId
        return null;

    }
}
