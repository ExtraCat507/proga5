package org.xtracat.server.commands;

import org.xtracat.client.util.Request;
import org.xtracat.client.util.Response;
import org.xtracat.datatypes.MusicBand;
import org.xtracat.CollectionManager;

import java.util.ArrayList;
import java.util.Comparator;

public class ServerShowCommand implements ServerCommand {
    private final CollectionManager cm;

    public ServerShowCommand(CollectionManager collectionManager) {
        this.cm = collectionManager;
    }

    @Override
    public Response execute(Request request) {
        try {
            ArrayList<MusicBand> bandList = new ArrayList<>();
            bandList.addAll(cm.getList());
            bandList.sort(Comparator.comparing(MusicBand::getName));
            return new Response("Список элементов коллекции:", bandList);
        } catch (Exception e) {
            e.printStackTrace();
            return new Response("Ошибка при получении коллекции: ");
        }
    }
}
