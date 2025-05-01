package com.nata.sockets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

public class BirdFeeder {

    private OutputStream out;
    private InputStream in;

    public void startConnection(String ip, int port) throws IOException {
        Socket clientSocket = new Socket(ip, port);

        out = clientSocket.getOutputStream();
        in = clientSocket.getInputStream();
    }

    public void sendMessage(String msg) throws IOException {
        out.write(msg.getBytes());
        dump(in);
    }

    static void dump(InputStream is) throws IOException {
        try (var reader = new InputStreamReader(is);
        var bufferedReader = new BufferedReader(reader)) {
            var line = "";
            while ((line = bufferedReader.readLine()) != null) {
                System.out.println(line);
            }
        }
    }

    public static void main(String[] args) throws IOException {
        BirdFeeder client = new BirdFeeder();
        client.startConnection("127.0.0.1", 8080);
        client.sendMessage("GET /sound HTTP/1.1\n\n");
    }
}
