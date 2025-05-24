package org.xtracat;

import org.xtracat.client.util.Request;
import org.xtracat.client.util.Response;
import org.xtracat.server.commands.*;

import java.util.HashMap;
import java.util.Map;

public class CommandTransferer {
    static Map<String, ServerCommand> commands = new HashMap<>();
    public CommandTransferer(String[] args){
        String filename;
        if (args.length != 0) {
            filename = args[0];
            System.out.println(filename);
        } else {
            filename = "collection.xml";
        }
        CollectionManager cm = new CollectionManager();


        commands.put("add",new ServerAddCommand(cm));
        commands.put("clear",new ServerClearCommand(cm));
        commands.put("count_greater_than_number_of_participants", new ServerCountGreaterThanNumberOfParticipants(cm));
        commands.put("print_ascending_num_of_participants", new ServerPrintAscendingListNumOfParticipants(cm));
        commands.put("filter_greater_than_label", new ServerFilterGreaterThanLabel(cm));
        commands.put("info", new ServerInfoCommand(cm));
        commands.put("remove_by_id", new ServerRemoveByIdCommand(cm));
        commands.put("remove_at_index", new ServerRemoveByIndexCommand(cm));
      //  commands.put("remove_last", new ServerRemoveLastCommand(cm));
        commands.put("exit", new ServerSaveCommand(cm));
        commands.put("show", new ServerShowCommand(cm));
        commands.put("shuffle", new ServerShuffleCommand(cm));
        commands.put("update", new ServerUpdateCommand(cm));
    }


    public static Response getResponse(Request request) {
        ServerCommand command = commands.get(request.getCommand());
        if(command == null){
            System.out.println(request);
        }
        return command.execute(request);
    }

}