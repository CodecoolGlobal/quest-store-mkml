package com.queststore.Services;


import com.queststore.DAO.*;
import com.queststore.Model.Class;
import com.queststore.Model.User;
import com.queststore.Model.UserType;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

import javax.jws.soap.SOAPBinding;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class UserCardAdd implements HttpHandler {

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        JSONparser jsonParser = new JSONparser();
        List<String> items = new ArrayList<>();
        List<User> userList = new ArrayList<>();


        try{
            items = jsonParser.parseJSONlistToArray(jsonParser.convertJSONtoString(httpExchange));
        } catch (IOException e) {
            e.printStackTrace();
        }
        String response = "";

        String[] splited = items.get(0).split("\\s+");
            items.remove(0);
            if(splited.length==2) {
            items.addAll(Arrays.asList(splited));
        }else if(splited.length==1){
            items.addAll(Arrays.asList(splited));
            items.add("UNDEFINDED");
        }

        try {

            userList = addUser(items);
        } catch (DaoException e) {
            e.printStackTrace();
        }

        JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/mentor-students.twig");
        JtwigModel model = JtwigModel.newModel();
        model.with("usersList", userList);
        response = template.render(model);

        // send the results to a the client
        httpExchange.sendResponseHeaders(200, response.length());
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    private List<User> addUser(List<String> items) throws DaoException {
        UserDAO addUser = new UserDAOSql();
        List<User> userList = new ArrayList<>();
        addUser.add(createStudent(items),"kawa");
        userList.addAll(addUser.getAllUser("student"));
        return userList;
    }

    private User createStudent (List<String> items) throws DaoException {
        ClassDAO classDAO = new ClassDAOSql();

        User newUser = new User.UserBuilder()
                .firstName(items.get(items.size()-2))
                .lastName(items.get(items.size()-1))
                .email(items.get(0))
                .userClass(new Class(classDAO.getClassId(items.get(1)).get(),items.get(1)))
                .userType(new UserType(1, "student")).createUser();
        return newUser;
    }
}