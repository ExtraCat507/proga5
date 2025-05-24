package org.xtracat.client.commands;

import org.xtracat.client.util.MyDispatcher;
import org.xtracat.client.util.Request;
import org.xtracat.client.util.Response;
import org.xtracat.usershit.User;

public class ClearCommand implements Command {

    private final MyDispatcher dispatcher;

    public ClearCommand(MyDispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    @Override
    public void prepare() {
        // Для команды clear подготовка не требуется
    }

    @Override
    public Request buildRequest() {
        return new Request("clear", null);
    }

    @Override
    public void processResponse(Response response) {
        if (response != null) {
            System.out.println("Коллекция очищена");
        } else {
            System.out.println("Нет ответа от сервера.");
        }
    }

    @Override
    public void execute(int mode, User user, String[] args) {
        prepare();
        Request request = buildRequest();
        request.setUser(user);
        Response response = dispatcher.send(request);
        processResponse(response);
    }

    @Override
    public String descr() {
        return "clear - удаляет все элементы коллекции.";
    }
}
