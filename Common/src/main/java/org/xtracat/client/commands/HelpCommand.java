package org.xtracat.client.commands;

import java.util.Map;
import org.xtracat.client.util.Dispatcher;
import org.xtracat.client.util.Request;
import org.xtracat.client.util.Response;

public class HelpCommand implements Command {

    private final Map<String, Command> commands;

    public HelpCommand(Dispatcher dispatcher, Map<String, Command> commands) {
        this.commands = commands;
    }

    @Override
    public void prepare() {
        // Подготовка не требуется для help
    }

    @Override
    public Request buildRequest() {
        // Help пока работает только на клиенте, Request не нужен
        return null;
    }

    @Override
    public void processResponse(Response response) {
        // Выполняем вывод команд прямо здесь, без ожидания ответа от сервера
        for (Command c : commands.values()) {
            System.out.println(c.descr());
        }
    }

    @Override
    public void execute(int mode, String[] args) {
        prepare();
        processResponse(null);
    }

    @Override
    public String descr() {
        return "help - возвращает список всех доступных команд";
    }
}
