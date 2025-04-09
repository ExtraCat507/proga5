package org.xtracat;


import org.xtracat.connection.util.ClientData;

import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;

import java.nio.channels.*;
import java.util.Set;

import static java.nio.channels.SelectionKey.*;

public class Main {
    public static void main(String[] args) {

        System.out.println("Server side running");
        InetAddress host;
        int port = 6789;

        try {
            Selector selector = Selector.open();
            ServerSocketChannel server = ServerSocketChannel.open();
            server.configureBlocking(false);
            server.register(selector, OP_ACCEPT);
            server.bind(new InetSocketAddress(port));
            while (true) {
                selector.select();
                Set<SelectionKey> keys = selector.selectedKeys();
                for (var iter = keys.iterator(); iter.hasNext(); ) {
                    SelectionKey key = iter.next();
                    iter.remove();
                    if (key.isValid()) {
                        if (key.isAcceptable()) {
                            doAccept(key);
                        }

                        if (key.isReadable()) {
                            doRead(key);
                            // ОБРАБОТКА
                            processData((ClientData) key.attachment());
                        }

                        if (key.isWritable()) {
                            doWrite(key);
                        }

                    }
                }
            }
            //selector.close();
        } catch (BindException e) {
            System.out.println("Порт 6789 уже занят");
        } catch (Exception e) {
            System.out.println("Гена все хуйня в целом");
            e.printStackTrace();
        }

    }

    private static void doAccept(SelectionKey key) {
        try {
            var ssc = (ServerSocketChannel) key.channel();      // с try with resources не работает
            SocketChannel sc = ssc.accept();
            ClientData clientData = new ClientData();
            sc.configureBlocking(false);

            SelectionKey nk = sc.register(key.selector(), OP_READ);
            nk.attach(clientData);
            key.selector().wakeup();

        } catch (IOException e) {
            System.out.println("Гена все хуйня в Accept");
        }
    }

    private static void doRead(SelectionKey key) {
        try {
            var sc = (SocketChannel) key.channel();
            var data = (ClientData) key.attachment();
            sc.read(data.buffer);
            SelectionKey nk = sc.register(key.selector(), OP_WRITE);
            nk.attach(data);

        } catch (IOException e) {
            System.out.println("Гена все хуйня в Read");
        }

    }

    private static void doWrite(SelectionKey key) {
        try {
            var sc = (SocketChannel) key.channel();
            var data = (ClientData) key.attachment();
            data.buffer.flip();
            sc.write(data.buffer);
            data.buffer.compact();
        } catch (IOException e) {
            System.out.println("Гена все хуйня в Write");
        }
    }

    private static ByteBuffer processData(ClientData data) {
        ByteBuffer buffer = data.buffer;
        buffer.flip();
        byte[] numbers = new byte[buffer.remaining()];
        ByteBuffer nbuffer = ByteBuffer.wrap(numbers);

        for (int i = buffer.position(); i < buffer.remaining(); i++) {
            byte kk = buffer.get(i);
            System.out.println(kk);
            numbers[i] = (byte) (kk * 2);
        }


        buffer.clear();  // Переключаем обратно в режим записи
        buffer.put(nbuffer);  // Записываем числа обратно в буфер


        return buffer;
    }
}