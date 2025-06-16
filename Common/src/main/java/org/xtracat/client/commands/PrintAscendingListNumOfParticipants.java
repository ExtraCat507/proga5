package org.xtracat.client.commands;

import org.xtracat.client.util.MyDispatcher;
import org.xtracat.client.util.Request;
import org.xtracat.client.util.Response;
import org.xtracat.usershit.User;

public class PrintAscendingListNumOfParticipants implements Command {

    private final MyDispatcher dispatcher;

    public PrintAscendingListNumOfParticipants(MyDispatcher dispatcher) {
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

        Object data = response.getData( );
        if (data instanceof long[]) {
            long[] numbers = (long[]) data;
            if (numbers.length == 0) {
                System.out.println("Коллекция пуста.");
            } else {
                System.out.println(response.getMessage());
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
    public void execute(int mode, User user, String[] args) {
        prepare();
        Request request = buildRequest();
        request.setUser(user);
        Response response = dispatcher.send(request);
        processResponse(response);
    }

    @Override
    public String descr() {
        return "printFieldAscendingNumberOfParticipants - вывести значения всех numOfParticipants в порядке неубывания";
    }
}
