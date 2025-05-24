package org.xtracat.server.commands;

import org.xtracat.client.util.Request;
import org.xtracat.client.util.Response;
import org.xtracat.CollectionManager;

public class ServerRemoveByIdCommand implements ServerCommand {
    private final CollectionManager cm;

    public ServerRemoveByIdCommand(CollectionManager collectionManager) {
        this.cm = collectionManager;
    }

    @Override
    public Response execute(Request request) {
        try {
            long id =(long) request.getContent();
            if (cm.findById(id) == -1) {
                return new Response("Ошибка: элемент с id " + id + " не существует");
            }
            int callback = cm.removeById(id,request.getUser().login());
            if (callback == 0) {
                return new Response("Элемент успешно удален");
            } else {
                return new Response("Ошибка удаления: callback = " + callback);
            }
        } catch (NumberFormatException e) {
            return new Response("Ошибка: аргумент должен быть числом");
        } catch (Exception e) {
            return new Response("Ошибка при выполнении команды: " + e.getMessage());
        }
    }
}
