package org.xtracat.server.commands;

import org.xtracat.client.util.Request;
import org.xtracat.client.util.Response;
import org.xtracat.CollectionManager;

public class ServerShuffleCommand implements ServerCommand {
    private final CollectionManager cm;

    public ServerShuffleCommand(CollectionManager collectionManager) {
        this.cm = collectionManager;
    }

    @Override
    public Response execute(Request request) {
        try {
            cm.shuffle();
            return new Response("Перемешано");
        } catch (Exception e) {
            return new Response("Ошибка при перемешивании: " + e.getMessage());
        }
    }
}
