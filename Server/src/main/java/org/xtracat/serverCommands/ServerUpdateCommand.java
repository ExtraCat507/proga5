package org.xtracat.serverCommands;

import org.xtracat.client.util.Request;
import org.xtracat.client.util.Response;
import org.xtracat.datatypes.MusicBand;
import org.xtracat.server.CollectionManager;

public class ServerUpdateCommand implements ServerCommand {
    private final CollectionManager cm;

    public ServerUpdateCommand(CollectionManager collectionManager) {
        this.cm = collectionManager;
    }

    @Override
    public Response execute(Request request) {
        try {
            Object[] payload = (Object[]) request.getContent();
            long id = (Long) payload[0];
            MusicBand newBand = (MusicBand) payload[1];

            if (cm.findById(id) == -1) {
                return new Response("Ошибка: элемента с таким id не существует.");
            }

            int callback = cm.changeById(id, newBand);
            if (callback == 0) {
                return new Response("Элемент успешно обновлён.");
            } else {
                return new Response("Ошибка при обновлении элемента.");
            }
        } catch (ClassCastException | NullPointerException e) {
            return new Response("Ошибка: неверный формат данных запроса.");
        } catch (Exception e) {
            return new Response("Неизвестная ошибка: " + e.getMessage());
        }
    }
}
