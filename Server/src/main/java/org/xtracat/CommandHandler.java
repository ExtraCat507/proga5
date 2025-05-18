package org.xtracat;

import org.xtracat.client.util.Request;
import org.xtracat.client.util.Response;
import org.xtracat.server.CollectionManager;
import org.xtracat.server.commands.*;

import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.xtracat.singleton.SingletonLogger;

public class CommandHandler {
    private Map<String, ServerCommand> commands = new HashMap<>();
    private final Logger logger = SingletonLogger.getLogger();

    public CommandHandler(String filename, CollectionManager cm){
        commands.put("add", new ServerAddCommand(cm));
        commands.put("clear", new ServerClearCommand(cm));
        commands.put("count_greater_than_number_of_participants", new ServerCountGreaterThanNumberOfParticipants(cm));
        commands.put("print_ascending_num_of_participants", new ServerPrintAscendingListNumOfParticipants(cm));
        commands.put("filter_greater_than_label", new ServerFilterGreaterThanLabel(cm));
        commands.put("info", new ServerInfoCommand(cm));
        commands.put("remove_by_id", new ServerRemoveByIdCommand(cm));
        commands.put("remove_at_index", new ServerRemoveByIndexCommand(cm));
        commands.put("remove_last", new ServerRemoveLastCommand(cm));
        commands.put("exit", new ServerSaveCommand(filename, cm));
        commands.put("show", new ServerShowCommand(cm));
        commands.put("shuffle", new ServerShuffleCommand(cm));
        commands.put("update", new ServerUpdateCommand(cm));
    }


    private ServerCommand get(String stringCommand) {
        return commands.get(stringCommand);
    }

    public Response getResponse(Request request) {
        logger.info("Got new task on backend: {}", request);
        ServerCommand command = this.get(request.getCommand());
        if (command == null) {
            logger.error("Unknown command in request: {}", request);
            return null;
        }
        Response response = command.execute(request);
        logger.info("Got response from backend: {}", response);
        return response;
    }


}
