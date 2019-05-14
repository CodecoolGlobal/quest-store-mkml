package com.queststore.helpers;

import org.junit.Test;

import java.net.HttpCookie;
import java.util.List;

import static org.junit.Assert.*;

public class CookieHelperTest {

    @Test
    public void parseCookies() {
        CookieHelper cookieHelper = new CookieHelper();
        List<HttpCookie> cookies = cookieHelper.parseCookies("session_id=\"c212a7ae-f42b-4b3d-ace5-3b76711867fe\";test_id=\"asdasdasdasdasdasdsadasdasdas\"");

        assertEquals("c212a7ae-f42b-4b3d-ace5-3b76711867fe", cookies.get(0).getValue());
        assertEquals("asdasdasdasdasdasdsadasdasdas", cookies.get(1).getValue());

    }
}