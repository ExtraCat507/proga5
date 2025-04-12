package org.xtracat.serverCommands;

import org.xtracat.client.util.Request;
import org.xtracat.client.util.Response;
import org.xtracat.server.CollectionManager;

public class ServerClearCommand implements ServerCommand {
    CollectionManager cm;

    public ServerClearCommand(CollectionManager collectionManager) {
        this.cm = collectionManager;
    }


    @Override
    public Response execute(Request request) {
        try {
            cm.clearCollection();
            return new Response("Успешное очищение коллекции");
        } catch (Exception e) {
            return new Response("Что-то пошло не так");
        }
    }
}
