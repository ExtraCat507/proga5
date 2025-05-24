package org.xtracat.client.commands;

import org.xtracat.client.util.Request;
import org.xtracat.client.util.Response;
import org.xtracat.usershit.User;

public interface Command { // Abstract Command
    void execute(int mode, User user, String[] args);

    void prepare();

    Request buildRequest();

    void processResponse(Response response);

    String descr(); // описание команды
}