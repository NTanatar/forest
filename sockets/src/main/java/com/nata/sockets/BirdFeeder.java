package com.nata.sockets;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import lombok.Getter;

@Getter
public class BirdFeeder {

    static void feedBird() throws IOException {
        String GET_REQUEST = "GET /bird/sound HTTP/1.1\r\nHost:localhost\r\n\r\n";
        Socket socket = new Socket("localhost", 8080);
        InputStream is = socket.getInputStream();
        OutputStream os = socket.getOutputStream();

        try {
            os.write(GET_REQUEST.getBytes());
            socket.shutdownOutput();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        int b;
        while (true) {
            try {
                if ((b = is.read()) == -1) {
                    break;
                }
                System.out.print((char) b);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }


    public static void main(String[] args) throws IOException {
        feedBird();
    }
}
