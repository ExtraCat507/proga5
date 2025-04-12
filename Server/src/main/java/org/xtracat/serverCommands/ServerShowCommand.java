package org.xtracat.serverCommands;

import org.xtracat.client.util.Request;
import org.xtracat.client.util.Response;
import org.xtracat.datatypes.MusicBand;
import org.xtracat.server.CollectionManager;

import java.util.LinkedList;

public class ServerShowCommand implements ServerCommand {
    private final CollectionManager cm;

    public ServerShowCommand(CollectionManager collectionManager) {
        this.cm = collectionManager;
    }

    @Override
    public Response execute(Request request) {
        try {
            LinkedList<MusicBand> bandList = cm.getList();
            return new Response("Список элементов коллекции:", bandList);
        } catch (Exception e) {
            return new Response("Ошибка при получении коллекции: " + e.getMessage());
        }
    }
}
