package org.xtracat.serverCommands;

import org.xtracat.client.util.Request;
import org.xtracat.client.util.Response;
import org.xtracat.datatypes.MusicBand;
import org.xtracat.server.CollectionManager;

public class ServerCountGreaterThanNumberOfParticipants implements ServerCommand {
    private final CollectionManager cm;

    public ServerCountGreaterThanNumberOfParticipants(CollectionManager collectionManager) {
        this.cm = collectionManager;
    }

    @Override
    public Response execute(Request request) {
        try {
            String arg = (String) request.getContent();
            int number = Integer.parseInt(arg);
            int result = (int) cm.getList().stream().filter(s -> s.getNumberOfParticipants() > number).count();
            return new Response("Количество элементов с числом участников больше " + number + ": " + result);
        } catch (NumberFormatException e) {
            return new Response("Ошибка: аргумент должен быть целым числом");
        } catch (ArrayIndexOutOfBoundsException e) {
            return new Response("Ошибка: отсутствует аргумент");
        }
    }
}
