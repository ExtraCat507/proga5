package org.xtracat.UI.util;

import org.xtracat.auth.Authentificator;
import org.xtracat.client.commands.*;
import org.xtracat.client.util.Dispatcher;
import org.xtracat.usershit.User;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class ScriptExecutor {

    public static void executeScript(String filename,User user) throws Exception {

        File file = new File(filename);
        Scanner sc = new Scanner(file);
        Map<String, Command> commands = new HashMap<>();

        List<File> fileSet = new ArrayList<>();
        List<Scanner> scannerStack = new ArrayList<>(); // наш стек сканеров
        //scannerStack.add(sc);
        //fileSet.add(file);

        Dispatcher dispatcher = new Dispatcher();


        commands.put("help", new HelpCommand(dispatcher, commands));
        commands.put("add", new AddCommand(dispatcher, scannerStack));
        //commands.put("save", new SaveCommand(collectionManager));
        commands.put("exit", new ExitCommand(dispatcher));
        commands.put("show", new ShowCommand(dispatcher));
        commands.put("info", new InfoCommand(dispatcher));
        commands.put("update", new UpdateCommand(dispatcher, scannerStack));
        commands.put("remove_by_id", new RemoveByIdCommand(dispatcher));
        commands.put("clear", new ClearCommand(dispatcher));
        commands.put("remove_at_index", new RemoveByIndexCommand(dispatcher));
        commands.put("remove_last", new RemoveLastCommand(dispatcher));
        commands.put("shuffle", new ShuffleCommand(dispatcher));
        commands.put("execute_script", new ExecuteScriptCommand(dispatcher, commands, scannerStack, fileSet));
        commands.put("countGreaterThanNumberOfParticipants", new CountGreaterThanNumberOfParticipants(dispatcher));
        commands.put("filter_greater_than_label", new FilterGreaterThanLabel(dispatcher, scannerStack));
        commands.put("printFieldAscendingNumberOfParticipants", new PrintAscendingListNumOfParticipants(dispatcher));

        ExecuteScriptCommand starterCommand = new ExecuteScriptCommand(dispatcher, commands, scannerStack, fileSet);
        starterCommand.execute(1,user,new String[]{filename});


        while (true) {
                Scanner currentSc = scannerStack.get(scannerStack.size() - 1);
                try {
                    String line = currentSc.nextLine(); //ловим NOsuchelement

                    if (line.isEmpty()) continue;
                    String[] tokens = line.split(" ");
                    Command command = commands.get(tokens[0]);

                    if (command == null) {
                        continue;
                    }

                    // construct Request,
                    command.execute(1, user, Arrays.copyOfRange(tokens, 1, tokens.length));
                }catch (NoSuchElementException e){
                    throw new Exception("Выполнено");
                }
        }

    }

}


