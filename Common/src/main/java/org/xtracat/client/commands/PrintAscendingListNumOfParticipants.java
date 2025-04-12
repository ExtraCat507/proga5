package org.xtracat.client.commands;

import org.xtracat.client.util.Dispatcher;
import org.xtracat.client.util.Request;
import org.xtracat.client.util.Response;

public class PrintAscendingListNumOfParticipants implements Command {

    private final Dispatcher dispatcher;

    public PrintAscendingListNumOfParticipants(Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    @Override
    public void prepare() {
        // Дополнительная подготовка не требуется.
    }

    @Override
    public Request buildRequest() {
        return new Request("printFieldAscendingNumberOfParticipants", null);
    }

    @Override
    public void processResponse(Response response) {
        if (response == null) {
            System.out.println("Нет ответа от сервера.");
            return;
        }

        Object data = response.getData();
        if (data instanceof Long[]) {
            Long[] numbers = (Long[]) data;
            if (numbers.length == 0) {
                System.out.println("Коллекция пуста.");
            } else {
                System.out.println("Значения количества участников в порядке возрастания:");
                for (Long num : numbers) {
                    System.out.print(num + " ");
                }
                System.out.println();
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
        return "printFieldAscendingNumberOfParticipants - вывести значения всех numOfParticipants в порядке неубывания";
    }
}
