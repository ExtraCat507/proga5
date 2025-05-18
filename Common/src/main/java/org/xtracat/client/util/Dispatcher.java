package org.xtracat.client.util;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.concurrent.TimeUnit;

public class Dispatcher implements MyDispatcher{

    private static final String HOST = "localhost";
    private static final int PORT = 6789;
    int tries=0;

    public Response send(Request request) {
        try (
                Socket socket = new Socket(InetAddress.getLocalHost(), PORT);
                OutputStream out = socket.getOutputStream();
                InputStream in = socket.getInputStream();
        ) {

            //objectOut.writeObject(request);
            byte[] serializedRequest = serializeRequest(request);
            out.write(serializedRequest);
            out.flush();


            //Object response = objectIn.readObject();
            Object response = deserializeResponse(in.readAllBytes());
            if (response instanceof Response) {
                //System.out.println("приняли норм респонс");
                tries=0;
                return (Response) response;
            } else {
                System.err.println("Получен некорректный объект вместо Response.");
                return new Response();
            }

        } catch (IOException e) {
            // добавить проверку и автомат переподключения

            if(tries>0){
                System.out.println("Не удалось подключиться повторно. Выходим :(");
                System.exit(0);
            }
            System.out.println("Ошибка при отправке запроса на сервер:");
            System.out.println("Повторная попытка через 5 секунд");
            tries++;
            try {
                TimeUnit.SECONDS.sleep(5);
            }
            catch (InterruptedException err){
                System.out.println("Мы были прерваны другим потоком");
                System.exit(0);
            }
            return this.send(request);
        }
    }

    private byte[] serializeRequest(Request request){
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(request);
            byte[] bytes = bos.toByteArray();
            return bytes;

        } catch (IOException ignored) {}
        return null;
    }

    private Response deserializeResponse(byte[] arr){
        try (ByteArrayInputStream bis = new ByteArrayInputStream(arr);
             ObjectInputStream ois = new ObjectInputStream(bis);
        ) {

            Object obj = ois.readObject();
            if (obj instanceof Response) {
                return (Response) obj;
            } else {
                System.err.println("Deserialized object is not a Response.");
                return null;
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }


}
