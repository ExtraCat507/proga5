//package org.xtracat.server.commands;
//
//import org.xtracat.client.util.Request;
//import org.xtracat.client.util.Response;
//import org.xtracat.CollectionManager;
//
//public class ServerRemoveLastCommand implements ServerCommand {
//    private final CollectionManager cm;
//
//    public ServerRemoveLastCommand(CollectionManager collectionManager) {
//        this.cm = collectionManager;
//    }
//
//    @Override
//    public Response execute(Request request) {
//        try {
//            long callback = cm.removeLast();
//            if (callback >= 0) {
//                return new Response("Удаление успешно");
//            } else if (callback == -1) {
//                return new Response("Ошибка: коллекция уже пуста");
//            } else {
//                return new Response("Ошибка: неизвестный callback: " + callback);
//            }
//        } catch (Exception e) {
//            return new Response("Ошибка при выполнении команды: " + e.getMessage());
//        }
//    }
//}
