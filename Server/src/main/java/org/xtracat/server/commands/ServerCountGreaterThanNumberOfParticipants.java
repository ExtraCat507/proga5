package org.xtracat.server.commands;

import org.xtracat.client.util.Request;
import org.xtracat.client.util.Response;
import org.xtracat.CollectionManager;

public class ServerCountGreaterThanNumberOfParticipants implements ServerCommand {
    private final CollectionManager cm;

    public ServerCountGreaterThanNumberOfParticipants(CollectionManager collectionManager) {
        this.cm = collectionManager;
    }

    @Override
    public Response execute(Request request) {
        try {
            int number = (int) request.getContent();
            int result = (int) cm.getList().stream().filter(s -> s.getNumberOfParticipants() > number).count();
            Response response = new Response("Количество элементов с числом участников больше " + number + ": " + result);
            return response;
        } catch (NumberFormatException e) {
            return new Response("Ошибка: аргумент должен быть целым числом");
        } catch (ArrayIndexOutOfBoundsException e) {
            return new Response("Ошибка: отсутствует аргумент");
        }
    }
}
