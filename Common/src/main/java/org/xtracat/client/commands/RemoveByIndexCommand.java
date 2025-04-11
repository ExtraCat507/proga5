package org.xtracat.client.commands;

import org.xtracat.client.util.Dispatcher;
import org.xtracat.client.util.Request;
import org.xtracat.client.util.Response;

public class RemoveByIndexCommand implements Command {

    private final Dispatcher dispatcher;
    private int index;

    public RemoveByIndexCommand(Dispatcher dispatcher) {
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
    public void execute(int mode, String[] args) {
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
        Response response = dispatcher.send(request);
        processResponse(response);
    }

    @Override
    public String descr() {
        return "remove_at_index - удалить по индексу списка";
    }
}
