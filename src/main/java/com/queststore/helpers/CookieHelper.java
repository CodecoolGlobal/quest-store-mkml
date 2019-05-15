package com.queststore.helpers;

import com.sun.net.httpserver.HttpExchange;

import java.net.HttpCookie;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class CookieHelper {

    private static final String SESSION_COOKIE_NAME = "session_id";

    public List<HttpCookie> parseCookies(String cookieStr) {
        List<HttpCookie> cookies = new ArrayList<>();

        if (cookieStr == null || cookieStr.isEmpty()) {
            return cookies;
        }

        for (String cookie : cookieStr.split(";")) {
            String[] pair = cookie.split("=");
            cookies.add(new HttpCookie(pair[0].trim(), pair[1].trim().replaceAll("\"", "")));
        }
        return cookies;
    }

    public Optional<HttpCookie> getCookieByName(List<HttpCookie> cookies) {
        for (HttpCookie cookie : cookies) {
            if (cookie.getName().equals(SESSION_COOKIE_NAME)) {
                return Optional.of(cookie);
            }
        }
        return Optional.empty();
    }

    public HttpCookie generateNewSessionIdCookie() {
        UUID uuid = UUID.randomUUID();
        return new HttpCookie("session_id", uuid.toString());
    }

    public Optional<HttpCookie> getSessionIdCookie(HttpExchange exchange) {
        String cookieStr = exchange.getRequestHeaders().getFirst("Cookie");
        System.out.println(cookieStr);
        List<HttpCookie> cookies = parseCookies(cookieStr);
        return getCookieByName(cookies);
    }
}
