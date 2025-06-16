package org.xtracat.client.commands;

import org.xtracat.client.util.MyDispatcher;
import org.xtracat.client.util.Request;
import org.xtracat.client.util.Response;
import org.xtracat.usershit.User;

public class CountGreaterThanNumberOfParticipants implements Command {

    private final MyDispatcher dispatcher;
    private int number;

    public CountGreaterThanNumberOfParticipants(MyDispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    @Override
    public void prepare() {
        // Подготовка не требуется, так как данные уже валидируются в execute
    }

    @Override
    public Request buildRequest() {
        return new Request("countGreaterThanNumberOfParticipants", number);
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
            number = Integer.parseInt(args[0]);
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
            return "countGreaterThanNumberOfParticipants num - возвращает число элементов, где NumberOfParticipants больше заданного";
    }
}
