package org.xtracat;

import org.xtracat.client.util.Request;
import org.xtracat.client.util.Response;
import org.xtracat.connection.util.ClientData;
import org.xtracat.server.CollectionManager;
import org.xtracat.server.commands.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.*;

import static java.nio.channels.SelectionKey.*;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        int port = 6789;
        String filename = (args.length != 0) ? args[0] : "collection.xml";
        logger.info("Using collection file: {}", filename);
        CollectionManager cm = new CollectionManager(filename);

        Map<String, ServerCommand> commands = new HashMap<>();
        commands.put("add", new ServerAddCommand(cm));
        commands.put("clear", new ServerClearCommand(cm));
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

        try {
            Selector selector = Selector.open();
            ServerSocketChannel server = ServerSocketChannel.open();
            server.configureBlocking(false);
            server.bind(new InetSocketAddress(port));
            server.register(selector, OP_ACCEPT);
            logger.info("Server started and listening on port {}", port);

            Pipe consolePipe = Pipe.open();
            Pipe.SourceChannel consoleSource = consolePipe.source();
            consoleSource.configureBlocking(false);
            consoleSource.register(selector, SelectionKey.OP_READ, "console");

            Pipe.SinkChannel consoleSink = consolePipe.sink();
            new Thread(() -> {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        byte[] bytes = (line + "\n").getBytes();
                        ByteBuffer buf = ByteBuffer.wrap(bytes);
                        while (buf.hasRemaining()) {
                            consoleSink.write(buf);
                        }
                    }
                } catch (IOException e) {
                    logger.error("Error reading from System.in", e);
                }
            }).start();

            while (true) {
                selector.select();
                Set<SelectionKey> keys = selector.selectedKeys();
                for (Iterator<SelectionKey> iter = keys.iterator(); iter.hasNext(); ) {
                    SelectionKey key = iter.next();
                    iter.remove();

                    if (!key.isValid())
                        continue;

                    if (key.isReadable() && "console".equals(key.attachment())) {
                        handleConsoleInput((Pipe.SourceChannel) key.channel(), filename, cm);
                        continue;
                    }

                    if (key.isAcceptable()) {
                        doAccept(key);
                    }

                    if (key.isReadable()) {
                        doRead(key);
                        if (!key.isValid())
                            continue;
                    }

                    if (key.isWritable()) {
                        Request request = parseRequest((ClientData) key.attachment());
                        logger.info("Received request: {}", request);
                        Response response = getResponse(request, commands);
                        ClientData data = (ClientData) key.attachment();
                        data.buffer.clear();
                        byte[] serializedResponse = serializeResponse(response);
                        if (serializedResponse != null) {
                            data.buffer.put(serializedResponse);
                            data.buffer.flip();
                        } else {
                            logger.error("Failed to serialize Response");
                        }
                        key.attach(data);
                        doWrite(key);
                    }
                }
            }
        } catch (BindException e) {
            logger.error("Port {} is busy", port, e);
        } catch (NoSuchElementException e) {
            ServerSaveCommand svc = new ServerSaveCommand(filename, cm);
            svc.execute(new Request(null, null));
            logger.info("Exiting");
            System.exit(0);
        } catch (Exception e) {
            logger.error("An error occurred in the server loop", e);
        }
    }

    private static void handleConsoleInput(Pipe.SourceChannel consoleSource, String filename, CollectionManager cm) {
        ByteBuffer buffer = ByteBuffer.allocate(256);
        try {
            int bytesRead = consoleSource.read(buffer);
            if (bytesRead > 0) {
                buffer.flip();
                byte[] bytes = new byte[buffer.remaining()];
                buffer.get(bytes);
                String command = new String(bytes).trim();
                logger.info("Console command received: {}", command);
                switch (command.toLowerCase()) {
                    case "exit":
                        logger.info("Exiting server per console command");
                        ServerSaveCommand exitCommand = new ServerSaveCommand(filename, cm);
                        exitCommand.execute(new Request("exit", null));
                        System.exit(0);
                        break;
                    case "save":
                        logger.info("Saving server state per console command");
                        ServerSaveCommand saveCommand = new ServerSaveCommand(filename, cm);
                        saveCommand.execute(new Request("save", null));
                        break;
                    default:
                        logger.warn("Unknown console command: {}", command);
                }
            }
            buffer.clear();
        } catch (IOException e) {
            logger.error("Error handling console input", e);
        }
    }

    private static void doAccept(SelectionKey key) {
        try {
            ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
            SocketChannel sc = ssc.accept();
            ClientData clientData = new ClientData();
            sc.configureBlocking(false);
            SelectionKey nk = sc.register(key.selector(), 0);
            nk.interestOps(OP_READ);
            nk.attach(clientData);
            key.selector().wakeup();
            logger.info("Accepted new client connection");
        } catch (IOException e) {
            logger.error("Error in Accept", e);
        }
    }

    private static void doRead(SelectionKey key) {
        try {
            SocketChannel sc = (SocketChannel) key.channel();
            ClientData data = (ClientData) key.attachment();
            int bytesRead = sc.read(data.buffer);
            if (bytesRead == -1) {
                logger.info("Client closed connection");
                sc.close();
                key.cancel();
                return;
            }
            key.interestOps(OP_WRITE);
        } catch (SocketException e) {
            key.cancel();
        } catch (IOException e) {
            logger.error("Error in Read", e);
        }
    }

    private static void doWrite(SelectionKey key) {
        try {
            SocketChannel sc = (SocketChannel) key.channel();
            ClientData data = (ClientData) key.attachment();
            sc.write(data.buffer);
            logger.debug("Wrote {} bytes to client", data.buffer.limit());
            data.buffer.clear();
            sc.close();
            key.cancel();
        } catch (SocketException e) {
            key.cancel();
        } catch (IOException e) {
            logger.error("Error in Write", e);
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
                logger.error("Unsupported type - not Request");
                return null;
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Response getResponse(Request request, Map<String, ServerCommand> commands) {
        ServerCommand command = commands.get(request.getCommand());
        if (command == null) {
            logger.error("Unknown command in request: {}", request);
            return null;
        }
        return command.execute(request);
    }

    private static byte[] serializeResponse(Response response) {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(response);
            return bos.toByteArray();
        } catch (IOException ignored) {
        }
        return null;
    }
}
