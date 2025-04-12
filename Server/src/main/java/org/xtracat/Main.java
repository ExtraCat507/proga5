package org.xtracat;


import org.xtracat.client.util.Request;
import org.xtracat.client.util.Response;
import org.xtracat.connection.util.ClientData;
import org.xtracat.server.CollectionManager;
import org.xtracat.serverCommands.*;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;

import java.nio.channels.*;
import java.util.*;

import static java.nio.channels.SelectionKey.*;

public class Main {
    public static void main(String[] args) {

        System.out.println("Server side running");

        String filename;
        if (args.length != 0) {
            filename = args[0];
            System.out.println(filename);
        } else {
            filename = "collection.xml";
        }
        CollectionManager cm = new CollectionManager(filename);
        Map<String, ServerCommand> commands = new HashMap<>();
        commands.put("add",new ServerAddCommand(cm));
        commands.put("clear",new ServerClearCommand(cm));
        commands.put("count_greater_than_number_of_participants", new ServerCountGreaterThanNumberOfParticipants(cm));
        commands.put("print_ascending_num_of_participants", new ServerPrintAscendingListNumOfParticipants(cm));
        commands.put("filter_greater_than_label", new ServerFilterGreaterThanLabel(cm));
        commands.put("info", new ServerInfoCommand(cm));
        commands.put("remove_by_id", new ServerRemoveByIdCommand(cm));
        commands.put("remove_at_index", new ServerRemoveByIndexCommand(cm));
        commands.put("remove_last", new ServerRemoveLastCommand(cm));
        commands.put("exit", new ServerSaveCommand(filename, cm));
        commands.put("show", new ServerShowCommand(cm));
        commands.put("shuffle", new ServerShuffleCommand(cm));
        commands.put("update", new ServerUpdateCommand(cm));



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
                            if(! key.isValid()){
                                continue;
                            }
                        }

                        if (key.isWritable()) {
                            // ОБРАБОТКА
                            Request request = parseRequest((ClientData) key.attachment());
                            System.out.println(request);
                            Response response = getResponse(request,commands);
                            ClientData data = (ClientData) key.attachment();
                            data.buffer.clear();
                            data.buffer.put(serializeResponse(response));
                            data.buffer.flip();
                            key.attach(data);
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
            ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
            SocketChannel sc = ssc.accept();
            ClientData clientData = new ClientData();
            sc.configureBlocking(false);

            SelectionKey nk = sc.register(key.selector(), 0);
            nk.interestOps(SelectionKey.OP_READ);
            nk.attach(clientData);
            key.selector().wakeup();

        } catch (IOException e) {
            System.out.println("Гена ошибка в Accept");
        }
    }

    private static void doRead(SelectionKey key) {
        try {
            var sc = (SocketChannel) key.channel();
            var data = (ClientData) key.attachment();
            sc.read(data.buffer);
            key.interestOps(OP_WRITE);
        } catch (SocketException e) {
            key.cancel();
        } catch (IOException e) {
            System.out.println("Гена все хуйня в Read");
        }

    }

    private static void doWrite(SelectionKey key) {
        try {
            var sc = (SocketChannel) key.channel();
            var data = (ClientData) key.attachment();
            sc.write(data.buffer);
            data.buffer.clear();
            sc.close();
            key.cancel();
        } catch (SocketException e) {
            key.cancel();
        } catch (IOException e) {
            System.out.println("Гена все хуйня в Write");
        }
    }

    private static Request parseRequest(ClientData userData) {

        ByteBuffer buffer = userData.buffer;
        buffer.flip();

        byte[] arr = new byte[buffer.remaining()];
        buffer.get(arr);
        buffer.clear();
        try (ByteArrayInputStream bis = new ByteArrayInputStream(arr);
             ObjectInputStream ois = new ObjectInputStream(bis)) {

            Object obj = ois.readObject();
            if (obj instanceof Request) {
                return (Request) obj;
            } else {
                System.err.println("Deserialized object is not a Request.");
                return null;
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }


    private static Response getResponse(Request request, Map<String, ServerCommand> commands) {
        ServerCommand command = commands.get(request.getContent());
        return command.execute(request);

    }

    private static byte[] serializeResponse(Response response) {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(response);
            byte[] bytes = bos.toByteArray();
            return bytes;

        } catch (IOException ignored) {
        }
        return null;
    }

}
