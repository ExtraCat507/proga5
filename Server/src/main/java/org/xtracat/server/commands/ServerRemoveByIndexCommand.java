package org.xtracat.server.commands;

import org.xtracat.client.util.Request;
import org.xtracat.client.util.Response;
import org.xtracat.server.CollectionManager;

public class ServerRemoveByIndexCommand implements ServerCommand {
    private final CollectionManager cm;

    public ServerRemoveByIndexCommand(CollectionManager collectionManager) {
        this.cm = collectionManager;
    }

    @Override
    public Response execute(Request request) {
        try {
            String content = (String) request.getContent();
            int index = Integer.parseInt(content.trim());
            int callback = cm.removeByIndex(index);
            if (callback == 0) {
                return new Response("Элемент успешно удален");
            } else if (callback == -1) {
                return new Response("Ошибка: индекс вне диапазона");
            } else {
                return new Response("Ошибка: неизвестный callback: " + callback);
            }
        } catch (NumberFormatException e) {
            return new Response("Ошибка: аргумент должен быть числом");
        } catch (Exception e) {
            return new Response("Ошибка при выполнении команды: " + e.getMessage());
        }
    }
}
