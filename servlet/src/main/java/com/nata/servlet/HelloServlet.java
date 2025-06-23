package com.nata.servlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@WebServlet(name = "helloServlet", value = "/hello")
public class HelloServlet extends HttpServlet {

    private static final String COOKIE_NAME = "NataSessionId";
    private static final String NAME_PARAM = "name";

    private final Map<String, Session> sessionMap = new ConcurrentHashMap<>();

    public void init() {

    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        // --- get or create session
        String sessionIdFromCookie = getSessionId(request.getCookies());
        System.out.println("Session id from cookie: " + sessionIdFromCookie);
        Session session = (sessionIdFromCookie == null || !sessionMap.containsKey(sessionIdFromCookie))
            ? createNewSession()
            : sessionMap.get(sessionIdFromCookie);

        // --- if name is specified update the session data
        if (request.getParameter(NAME_PARAM) != null) {
            session.setUserName(request.getParameter(NAME_PARAM));
        }
        sessionMap.put(session.getSessionId(), session);

        System.out.println("-> stored: " + session.getSessionId() + ", " + session.getUserName());

        // ---- build response
        response.addCookie(new Cookie(COOKIE_NAME, session.getSessionId()));

        String name = session.getUserName();
        response.setContentType("text/html");

        PrintWriter out = response.getWriter();
        if (name != null) {
            out.println("Hello, " + name + "!");
        } else {
            out.println("Hello!");
        }
    }

    public void destroy() {
    }

    private String getSessionId(Cookie [] cookies) {
        if  (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(COOKIE_NAME)) {
                    return cookie.getValue();
                }
            }
        }
        return null;

        /*return ofNullable(cookies)
            .flatMap(arr -> Arrays.stream(arr)
                .filter(cookie -> cookie.getName().equals(COOKIE_NAME))
                .findFirst()
                .map(Cookie::getValue))
            .orElse(null);*/
    }

    private Session createNewSession() {
        UUID uuid = UUID.randomUUID();
        while (sessionMap.containsKey(uuid.toString())) {
            uuid = UUID.randomUUID();
        }
        System.out.println("-> New session Id: " + uuid);
        return new Session(uuid.toString());
    }
}