package org.xtracat;

import org.xtracat.client.util.Request;
import org.xtracat.client.util.Response;
import org.xtracat.server.commands.*;

import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.xtracat.logger.SingletonLogger;

public class CommandHandler {
    private Map<String, ServerCommand> commands = new HashMap<>();
    private final Logger logger = SingletonLogger.getLogger();

    public CommandHandler(CollectionManager cm){
        commands.put("add", new ServerAddCommand(cm));
        commands.put("clear", new ServerClearCommand(cm));
        commands.put("countGreaterThanNumberOfParticipants", new ServerCountGreaterThanNumberOfParticipants(cm));
        commands.put("printFieldAscendingNumberOfParticipants", new ServerPrintAscendingListNumOfParticipants(cm));
        commands.put("filterGreaterThanLabel", new ServerFilterGreaterThanLabel(cm));
        commands.put("info", new ServerInfoCommand(cm));
        commands.put("remove_by_id", new ServerRemoveByIdCommand(cm));
        commands.put("remove_at_index", new ServerRemoveByIndexCommand(cm));
        //commands.put("remove_last", new ServerRemoveLastCommand(cm));
        commands.put("exit", new ServerSaveCommand(cm));
        commands.put("show", new ServerShowCommand(cm));
        commands.put("shuffle", new ServerShuffleCommand(cm));
        commands.put("update", new ServerUpdateCommand(cm));
        commands.put("auth", new AuthCommand());
        commands.put("register",new RegisterCommand());
    }


    private ServerCommand get(String stringCommand) {
        return commands.get(stringCommand);
    }

    public Response getResponse(Request request) {
        ServerCommand command = this.get(request.getCommand());
        logger.info("Got new task on backend: {}", request);
        if (command == null) {
            logger.error("Unknown command in request: {}", request);
            return null;
        }
        Response response = command.execute(request);
        logger.info("Got response from backend: {}", response);
        return response;
    }


}
