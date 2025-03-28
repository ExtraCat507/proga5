package org.xtracat.client;


import org.xtracat.client.commands.*;
import org.xtracat.server.CollectionManager;

import java.io.File;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        String filename;
        Map<String, Command> commands = new HashMap<>();
        List<File> fileSet = new ArrayList<>();

        Scanner sc = new Scanner(System.in);
        List<Scanner> scannerStack = new ArrayList<>(); // наш стек сканеров
        scannerStack.add(sc);

        if (args.length != 0) {
            filename = args[0];
            System.out.println(filename);
        } else {
            filename = "collection.xml";
        }
        CollectionManager collectionManager = new CollectionManager(filename);

        commands.put("help", new HelpCommand(commands));
        commands.put("add", new AddCommand(filename, collectionManager, scannerStack));
        commands.put("save", new SaveCommand(filename, collectionManager));
        commands.put("exit", new ExitCommand());
        commands.put("show", new ShowCommand(filename, collectionManager));
        commands.put("info", new InfoCommand(filename, collectionManager));
        commands.put("update", new UpdateCommand(filename, collectionManager, scannerStack));
        commands.put("remove_by_id", new RemoveByIdCommand(filename, collectionManager));
        commands.put("clear", new ClearCommand(filename, collectionManager));
        commands.put("remove_at_index", new RemoveByIndexCommand(filename, collectionManager));
        commands.put("remove_last", new RemoveLastCommand(filename, collectionManager));
        commands.put("shuffle", new ShuffleCommand(filename, collectionManager));
        commands.put("execute_script", new ExecuteScriptCommand(commands, scannerStack, fileSet));
        commands.put("countGreaterThanNumberOfParticipants", new CountGreaterThanNumberOfParticipants(filename, collectionManager));
        commands.put("filter_greater_than_label", new FilterGreaterThanLabel(filename, collectionManager, scannerStack));
        commands.put("printFieldAscendingNumberOfParticipants", new PrintAscendingListNumOfParticipants(filename, collectionManager));

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
                    command.execute(0, Arrays.copyOfRange(tokens, 1, tokens.length));
                } else {
                    if (command == null) {
                        throw new RuntimeException();
                    }
                    command.execute(1, Arrays.copyOfRange(tokens, 1, tokens.length));
                }

            } catch (NoSuchElementException e) {
                if (scannerStack.size() != 1) {
                    scannerStack.remove(scannerStack.size() - 1);
                    System.out.println("Закончили обработку файла " + fileSet.get(fileSet.size() - 1).getName());
                    fileSet.remove(fileSet.size() - 1);
                    continue;
                } else {
                    System.out.println("Лан, выхожу ");
                    break;
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Так как были переданы неверные аргументы, анализ файла завершен");
                scannerStack.subList(1, scannerStack.size()).clear();
                fileSet.clear();
                continue;
            } catch (RuntimeException e) {
                System.out.println("В файле(файлах) найдена неизвестная команда.\n Его обработка остановлена");
                scannerStack.subList(1, scannerStack.size()).clear();
                fileSet.clear();
                continue;
            }

        }
    }
}


//


//6) filter_greater_than_label не работает (non representative)
//7) Починить add в скрипте, убрать промпты для ввода