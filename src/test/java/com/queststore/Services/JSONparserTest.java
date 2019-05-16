package com.queststore.Services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import com.sun.net.httpserver.HttpExchange;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class JSONparserTest {

    private JSONparser jsonParser;

    @BeforeEach
    void setup() {
        jsonParser = new JSONparser();
    }

    @AfterEach
    void tearDown() {
        jsonParser = null;
    }

    @Test
    public void parseJSONlistToArray() throws java.io.IOException {
        String json = "[\n" +
                "        \"my/path/old\",\n" +
                "        \"my/path/new\"\n" +
                "    ]";
        System.out.println(jsonParser.parseJSONlistToArray(json));
        assertEquals("my/path/old", jsonParser.parseJSONlistToArray(json).get(0));
        assertEquals("my/path/new", jsonParser.parseJSONlistToArray(json).get(1));
    }

    @Test
    public void convertJSONtoString() throws IOException {
        HttpExchange httpExchangeMock = mock(HttpExchange.class);
        String bodyString = "sampleRequestBody";
        InputStream inputStream = new ByteArrayInputStream(bodyString.getBytes());
        when(httpExchangeMock.getRequestBody()).thenReturn(inputStream);
        assertEquals(bodyString, jsonParser.convertJSONtoString(httpExchangeMock));
    }
}