package org.xtracat.client.commands;

import org.xtracat.client.util.MyDispatcher;
import org.xtracat.client.util.Request;
import org.xtracat.client.util.Response;
import org.xtracat.usershit.User;

public class ExitCommand implements Command {

    private final MyDispatcher dispatcher;

    public ExitCommand(MyDispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    @Override
    public void prepare() {
        // Дополнительная подготовка не требуется.
    }

    @Override
    public Request buildRequest() {
        return new Request("exit", null);
    }

    @Override
    public void processResponse(Response response) {
        if (response != null) {
            System.out.println("Выходим из приложения");    //todo: добавить имя юзера
        } else {
            System.out.println("Нет ответа от сервера.");
        }
        System.exit(0); // Завершаем работу клиента
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
        return "exit - завершить работу клиента";
    }
}
