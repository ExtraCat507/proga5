package org.xtracat.client.commands;

import org.xtracat.client.util.MyDispatcher;
import org.xtracat.client.util.Request;
import org.xtracat.client.util.Response;
import org.xtracat.usershit.User;

public class RemoveByIndexCommand implements Command {

    private final MyDispatcher dispatcher;
    private int index;

    public RemoveByIndexCommand(MyDispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    @Override
    public void prepare() {
        // Дополнительная подготовка не требуется.
    }

    @Override
    public Request buildRequest() {
        return new Request("remove_at_index", index);
    }

    @Override
    public void processResponse(Response response) {
        if (response != null) {
            System.out.println(response.getMessage());
        } else {
            System.out.println("Нет ответа от сервера.");
        }
    }

    @Override
    public void execute(int mode, User user, String[] args) {
        try {
            index = Integer.parseInt(args[0]);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Нет аргумента");
            return;
        } catch (NumberFormatException e) {
            System.out.println("Неправильный формат аргумента");
            return;
        }
        prepare();
        Request request = buildRequest();
        request.setUser(user);
        Response response = dispatcher.send(request);
        processResponse(response);
    }

    @Override
    public String descr() {
        return "remove_at_index - удалить по индексу списка";
    }
}
