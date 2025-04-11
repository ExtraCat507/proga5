package org.xtracat.client.util;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class Dispatcher {

    private static final String HOST = "localhost";
    private static final int PORT = 6789;

    public Response send(Request request) {
        try (
                Socket socket = new Socket(InetAddress.getByName(HOST), PORT);
                OutputStream out = socket.getOutputStream();
                InputStream in = socket.getInputStream();
                ObjectOutputStream objectOut = new ObjectOutputStream(out);
                ObjectInputStream objectIn = new ObjectInputStream(in)
        ) {

            objectOut.writeObject(request);
            objectOut.flush();

            Object response = objectIn.readObject();
            if (response instanceof Response) {
                return (Response) response;
            } else {
                System.err.println("Получен некорректный объект вместо Response.");
                return new Response();
            }

        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Ошибка при отправке запроса на сервер:");
            return new Response();
        }
    }
}
