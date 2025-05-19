package com.nata.sockets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import lombok.Getter;

@Getter
public class BirdFeeder {

    static final String GET_REQUEST = "GET /bird/sound HTTP/1.1\r\nHost:localhost\r\n\r\n";

    static final String POST_REQUEST = "POST /bird/feed HTTP/1.1\r\nHost:localhost\r\nContent-Type: application/json\r\nContent-Length: 21\r\n\r\n{\"foodName\":\"bread\"}";

    static void send(String request) throws IOException {
        Socket socket = new Socket("localhost", 8080);
        InputStream is = socket.getInputStream();
        OutputStream os = socket.getOutputStream();

        try {
            os.write(request.getBytes());
            socket.shutdownOutput();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try (var reader = new InputStreamReader(is);
            var bufferedReader = new BufferedReader(reader)) {
            var line = "";
            while ((line = bufferedReader.readLine()) != null) {
                System.out.println(line);
            }
        }
        System.out.println();
    }

    public static void main(String[] args) throws IOException {
        System.out.println("----------------------- sending ");
        System.out.println(GET_REQUEST);
        System.out.println("----------------------> receiving:");
        send(GET_REQUEST);

        System.out.println("---------------------- sending ");
        System.out.println(POST_REQUEST);
        System.out.println("----------------------> receiving:");
        send(POST_REQUEST);
    }
}
