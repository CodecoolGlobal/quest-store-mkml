package com.queststore.Services;

import org.junit.Test;

import static org.junit.Assert.*;

public class JSONparserTest {

    @Test
    public void parseJSONlistToArray() throws java.io.IOException {
        JSONparser jsoNparser = new JSONparser();
        String json = "[\n" +
                "        \"my/path/old\",\n" +
                "        \"my/path/new\"\n" +
                "    ]";
        System.out.println(jsoNparser.parseJSONlistToArray(json));
        assertEquals("my/path/old", jsoNparser.parseJSONlistToArray(json).get(0));
        assertEquals("my/path/new", jsoNparser.parseJSONlistToArray(json).get(1));
    }
}