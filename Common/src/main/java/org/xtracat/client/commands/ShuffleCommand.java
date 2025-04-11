package org.xtracat.client.commands;

import org.xtracat.client.util.Dispatcher;
import org.xtracat.client.util.Request;
import org.xtracat.client.util.Response;

public class ShuffleCommand implements Command {

    private final Dispatcher dispatcher;

    public ShuffleCommand(Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    @Override
    public void prepare() {
        // Нет дополнительной подготовки.
    }

    @Override
    public Request buildRequest() {
        return new Request("shuffle", null);
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
        prepare();
        Request request = buildRequest();
        Response response = dispatcher.send(request);
        processResponse(response);
    }

    @Override
    public String descr() {
        return "shuffle - перемешать коллекцию";
    }
}
