package com.nata.servlet;

import lombok.Data;

@Data
public class Session {
    private String sessionId;
    private String userName;

    public Session(String sessionId) {
        this.sessionId = sessionId;
    }
}
