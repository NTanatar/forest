package com.nata.sockets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import lombok.Getter;

@Getter
public class BirdFeeder {

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

        try (Socket socket = new Socket("127.0.0.1", 8080)) {
            var writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            writer.println("GET /bird/sound HTTP/1.1");
            //writer.println("Host: localhost:8080");
            writer.flush();

            System.out.println("waiting for response...");
            dump(socket.getInputStream());
        }
    }
}
