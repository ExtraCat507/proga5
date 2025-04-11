package org.xtracat.client.commands;

import org.xtracat.client.util.Dispatcher;
import org.xtracat.client.util.Request;
import org.xtracat.client.util.Response;
import org.xtracat.datatypes.MusicBand;

import java.util.List;

public class ShowCommand implements Command {

    private final Dispatcher dispatcher;

    public ShowCommand(Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    @Override
    public void prepare() {
        // Нет дополнительной подготовки
    }

    @Override
    public Request buildRequest() {
        return new Request("show", null);
    }

    @Override
    public void processResponse(Response response) {
        if (response == null) {
            System.out.println("Нет ответа от сервера.");
            return;
        }
        Object data = response.getData();
        if (data instanceof List) {
            List<MusicBand> bandList = (List<MusicBand>) data;
            if (bandList.isEmpty()) {
                System.out.println("Коллекция пуста.");
            } else {
                System.out.println("****************************");
                for (MusicBand band : bandList) {
                    System.out.println(band);
                }
                System.out.println("****************************");
            }
        } else {
            System.out.println(response.getMessage());
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
        return "show - выводит все элементы коллекции";
    }
}
