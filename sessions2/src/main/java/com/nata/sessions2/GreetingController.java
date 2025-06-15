package com.nata.sessions2;

import static org.springframework.util.StringUtils.hasText;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class GreetingController {

    private final UserSessions userSessions;

    @GetMapping("/hello")
    @ResponseBody
    public String greet(HttpSession session, @RequestParam(required = false) String name) {
        System.out.println("session: " + session.getId());
        if (hasText(name)) {
            userSessions.storeSessionIfAbsent(session);
            userSessions.setUserName(session.getId(), name);
            return "Hello, " + name + "!";
        } else {
            String stored = userSessions.getUserNameIfExists(session.getId());
            if (hasText(stored)) {
                return "Hello, " + stored + "!";
            } else {
                return "Hello!";
            }
        }
    }
}
