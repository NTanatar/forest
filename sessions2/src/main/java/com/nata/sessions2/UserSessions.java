package com.nata.sessions2;

import static java.util.Optional.ofNullable;

import jakarta.servlet.http.HttpSession;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;

@Service
public class UserSessions {

    private final Map<String, HttpSession> sessions = new ConcurrentHashMap<>();

    public String getUserNameIfExists(String sessionId) {
        return ofNullable(sessions.get(sessionId))
            .map(session -> (User) session.getAttribute("user"))
            .map(User::getName)
            .orElse("");
    }

    public void storeSessionIfAbsent(HttpSession session) {
        sessions.putIfAbsent(session.getId(), session);
    }

    public void setUserName(String sessionId, String name) {
        HttpSession session = sessions.get(sessionId);

        User sessionUser = ofNullable((User) session.getAttribute("user"))
            .orElse(new User(name));

        sessionUser.setName(name);

        session.setAttribute("user", sessionUser);
    }
}
