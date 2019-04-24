package com.queststore.Controller;

import com.sun.net.httpserver.HttpExchange;
import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class JSONparser {

    public String convertJSONtoString(HttpExchange httpExchange) throws IOException {

        InputStreamReader isr = new InputStreamReader(httpExchange.getRequestBody(), "utf-8");
        BufferedReader br = new BufferedReader(isr);
        return br.readLine();
    }

    public List<String> parseJSONlistToArray(String JSONstring) throws IOException {

        List<String> itemsList = new ArrayList<>();
        JSONArray jsonArray = new JSONArray(JSONstring);
        List<Object> jsonList= jsonArray.toList();
        itemsList = jsonList.stream()
                .map(object -> Objects.toString(object, null))
                .collect(Collectors.toList());

        return itemsList;
    }
}
