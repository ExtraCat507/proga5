package org.xtracat;


import java.io.File;
import java.util.*;

import org.xtracat.auth.Authentificator;
import org.xtracat.client.commands.*;
import org.xtracat.client.util.Dispatcher;
import org.xtracat.usershit.UserRecord;

public class Main {
    public static void main(String[] args) {

        System.out.println("Clientside running");

        Map<String, Command> commands = new HashMap<>();

        Scanner sc = new Scanner(System.in);
        List<File> fileSet = new ArrayList<>();
        List<Scanner> scannerStack = new ArrayList<>(); // наш стек сканеров
        scannerStack.add(sc);

        Dispatcher dispatcher = new Dispatcher();

        Authentificator authentificator = new Authentificator(sc,dispatcher);
        UserRecord user =  authentificator.auth();


        commands.put("help", new HelpCommand(dispatcher,commands));
        commands.put("add", new AddCommand(dispatcher,scannerStack));
        //commands.put("save", new SaveCommand(collectionManager));
        commands.put("exit", new ExitCommand(dispatcher));
        commands.put("show", new ShowCommand(dispatcher));
        commands.put("info", new InfoCommand(dispatcher));
        commands.put("update", new UpdateCommand(dispatcher,scannerStack));
        commands.put("remove_by_id", new RemoveByIdCommand(dispatcher));
        commands.put("clear", new ClearCommand(dispatcher));
        commands.put("remove_at_index", new RemoveByIndexCommand(dispatcher));
        commands.put("remove_last", new RemoveLastCommand(dispatcher));
        commands.put("shuffle", new ShuffleCommand(dispatcher));
        commands.put("execute_script", new ExecuteScriptCommand(dispatcher,commands, scannerStack, fileSet));
        commands.put("countGreaterThanNumberOfParticipants", new CountGreaterThanNumberOfParticipants(dispatcher));
        commands.put("filter_greater_than_label", new FilterGreaterThanLabel(dispatcher,scannerStack));
        commands.put("printFieldAscendingNumberOfParticipants", new PrintAscendingListNumOfParticipants(dispatcher));

        while (true) {
            try {
                Scanner currentSc = scannerStack.get(scannerStack.size() - 1);
                if (scannerStack.size() == 1) { // пользовательский ввод
                    System.out.print(">");
                }

                String line = currentSc.nextLine(); //ловим NOsuchelement
                if (line.isEmpty()) continue;
                String[] tokens = line.split(" ");
                Command command = commands.get(tokens[0]);


                if (scannerStack.size() == 1) { // пользовательский ввод
                    if (command == null) {
                        System.out.println("Not a command");
                        continue;
                    }

                    // construct Request,
                    command.execute(0, Arrays.copyOfRange(tokens, 1, tokens.length));


                } else {
                    if (command == null) {
                       continue;
                    }

                    // construct Request,
                    command.execute(1, Arrays.copyOfRange(tokens, 1, tokens.length));
                }

            } catch (NoSuchElementException e) {
                if (scannerStack.size() != 1) {
                    scannerStack.remove(scannerStack.size() - 1);
                    System.out.println("Закончили обработку файла " + fileSet.get(fileSet.size() - 1).getName());
                    fileSet.remove(fileSet.size() - 1);
                    continue;
                } else {
                    ExitCommand interruption = new  ExitCommand(dispatcher);
                    interruption.execute(0,new String[0]);
                    System.out.println("Лан, выхожу ");
                    break;
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Так как были переданы неверные аргументы, анализ файла завершен");
                scannerStack.subList(1, scannerStack.size()).clear();
                fileSet.clear();
                continue;
            } catch (RuntimeException e) {
                e.printStackTrace();
                System.out.println("В файле(файлах) найдена неизвестная команда.\n Его обработка остановлена");
                scannerStack.subList(1, scannerStack.size()).clear();
                fileSet.clear();
                continue;
            }

        }

    }
}