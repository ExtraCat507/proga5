package org.xtracat.server.commands;

import org.xtracat.client.util.Request;
import org.xtracat.client.util.Response;
import org.xtracat.datatypes.Label;
import org.xtracat.datatypes.MusicBand;
import org.xtracat.server.CollectionManager;

import java.util.ArrayList;

public class ServerFilterGreaterThanLabel implements ServerCommand {
    private final CollectionManager cm;

    public ServerFilterGreaterThanLabel(CollectionManager collectionManager) {
        this.cm = collectionManager;
    }

    @Override
    public Response execute(Request request) {
        try {
            // Ожидается, что в request.getContent() передан объект Label
            Label comparedLabel = (Label) request.getContent();
            if (comparedLabel == null) {
                return new Response("Ошибка: переданный Label равен null");
            }
            ArrayList<MusicBand> result = (ArrayList<MusicBand>) cm.getList().stream().filter(s -> s.compareTo(comparedLabel) > 0);
            return new Response("Найдено элементов с лейблом больше заданного: " + result.size(), result);
        } catch (ClassCastException e) {
            return new Response("Ошибка: неверный тип данных в запросе, ожидался Label");
        } catch (Exception e) {
            return new Response("Ошибка при выполнении команды: " + e.getMessage());
        }
    }
}
