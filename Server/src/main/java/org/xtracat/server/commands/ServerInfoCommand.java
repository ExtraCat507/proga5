package org.xtracat.server.commands;

import org.xtracat.client.util.Request;
import org.xtracat.client.util.Response;
import org.xtracat.server.CollectionManager;

public class ServerInfoCommand implements ServerCommand {
    private final CollectionManager cm;

    public ServerInfoCommand(CollectionManager collectionManager) {
        this.cm = collectionManager;
    }

    @Override
    public Response execute(Request request) {
        try {
            String info = cm.getBandsCollection().toString();
            return new Response("Информация о коллекции:\n" + info);
        } catch (Exception e) {
            return new Response("Ошибка при получении информации о коллекции: " + e.getMessage());
        }
    }
}
