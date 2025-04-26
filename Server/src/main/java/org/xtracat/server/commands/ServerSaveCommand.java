package org.xtracat.server.commands;

import org.xtracat.client.util.Request;
import org.xtracat.client.util.Response;
import org.xtracat.server.CollectionManager;
import org.xtracat.storage.Serializer;

public class ServerSaveCommand implements ServerCommand {
    private final Serializer sr;
    private final String filename;
    private final CollectionManager cm;

    public ServerSaveCommand(String filename, CollectionManager collectionManager) {
        this.sr = new Serializer();
        this.filename = filename;
        this.cm = collectionManager;
    }

    @Override
    public Response execute(Request request) {
        try {
            int callback = sr.save(filename, cm);
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
