package com.queststore.helpers;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;

public class HttpUtils {

    public static void send500(HttpExchange httpExchange) throws IOException {
        String response = "500 Server internal error\n";
        httpExchange.sendResponseHeaders(500, response.length());
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.toString().getBytes());
        os.close();
    }

}
