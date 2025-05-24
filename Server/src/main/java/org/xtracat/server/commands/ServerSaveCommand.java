package org.xtracat.server.commands;

import org.xtracat.client.util.Request;
import org.xtracat.client.util.Response;
import org.xtracat.CollectionManager;

public class ServerSaveCommand implements ServerCommand {
    private final CollectionManager cm;

    public ServerSaveCommand(CollectionManager collectionManager) {
        this.cm = collectionManager;
    }

    @Override
    public Response execute(Request request) {
        try {
            //int callback = sr.save(filename, cm);
            int callback = 0;
            if (callback == 0) {
                return new Response("Готово");
            } else {
                return new Response("Ошибка при сохранении: callback = " + callback);
            }
        } catch (Exception e) {
            return new Response("Ошибка при выполнении команды: " + e.getMessage());
        }
    }
}
