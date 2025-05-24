package org.xtracat;

import org.xtracat.client.util.Request;
import org.xtracat.client.util.Response;
import org.xtracat.dao.DatabaseManager;
import org.xtracat.dao.SingletonDAO;
import org.xtracat.server.CollectionManager;
import org.xtracat.server.commands.*;
import org.slf4j.Logger;
import org.xtracat.logger.SingletonLogger;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;

import static java.nio.channels.SelectionKey.*;

public class Main {
    static final Logger logger = SingletonLogger.getLogger();

    private static final ForkJoinPool readPool = new ForkJoinPool();
    private static final ForkJoinPool execPool = new ForkJoinPool();
    private static final ExecutorService writePool = Executors.newFixedThreadPool(5);

    private static final String dbUrl = "jdbc:postgresql://localhost:5432/studs";
    private static final String dbUser = "s467467";

    public static void main(String[] args) {
        int port;
        Scanner scan = new Scanner(System.in);
        try  {
            System.out.println("Enter connection port:");
            port = scan.nextInt();
        } catch (Exception e) {
            System.out.println("Wrong input. Default port (6789) selected");
            port = 6789;
        }

        String filename = (args.length != 0) ? args[0] : "collection.xml";
        logger.info("Using collection file: {}", filename);
        Properties info = getDBPassword();

        DatabaseManager dao = SingletonDAO.getDao(dbUrl,info);
        CollectionManager cm = new CollectionManager(filename);
        CommandHandler commandHandler = new CommandHandler(filename, cm);
        ChannelManager channelManager = new ChannelManager();

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
            ServerConsole console = new ServerConsole(consoleSink);
            Thread consoleThread = new Thread(
                    console::run,
                    "ServerConsole-Thread"
            );
            consoleThread.setDaemon(true);
            consoleThread.start();


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
                        try {
                            ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
                            SocketChannel sc = ssc.accept();
                            sc.configureBlocking(false);
                            SelectionKey nk = sc.register(key.selector(), 0);
                            nk.interestOps(OP_READ);
                            key.selector().wakeup();
                            logger.info("Accepted new client connection");
                        } catch (IOException e) {
                            logger.error("Error in Accept", e);
                        }
                    }


                    if (key.isReadable()) {
                        readPool.submit(() -> {
                                    SocketChannel sc = (SocketChannel) key.channel();
                                    Request request = channelManager.readRequest(sc);
                                    logger.info("Received request: " + request );
                                    execPool.submit(() -> {
                                        Response response = commandHandler.getResponse(request);
                                        writePool.submit(() -> {
                                            channelManager.writeResponse(sc, response);
                                        });
                                    });
                                }
                        );
                        key.cancel();
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

    private static Properties getDBPassword(){
        Properties info = new Properties();
        try {
            info.load(new FileInputStream("db.cfg"));
        } catch (IOException e) {
            Scanner scc = new Scanner(System.in);
            System.out.println("Database config not found, enter username:");
            String user = scc.nextLine().trim();
            System.out.println("And password:");
            String password = scc.nextLine().trim();
            info.put("user",user);
            info.put("password",password);
        }
        return info;
    }

}
