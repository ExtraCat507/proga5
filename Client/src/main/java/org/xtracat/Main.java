package org.xtracat;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;
import java.util.*;

import org.xtracat.client.commands.*;
import org.xtracat.client.util.AdvancedScanner;
import org.xtracat.server.CollectionManager;

public class Main {
    public static void main(String[] args) {

        System.out.println("Clientside running");

        Map<String, Command> commands = new HashMap<>();

        Scanner sc = new Scanner(System.in);
        List<File> fileSet = new ArrayList<>();
        List<Scanner> scannerStack = new ArrayList<>(); // наш стек сканеров
        scannerStack.add(sc);


        commands.put("help", new HelpCommand(commands));
        commands.put("add", new AddCommand(scannerStack));
        //commands.put("save", new SaveCommand(collectionManager));
        commands.put("exit", new ExitCommand());
        commands.put("show", new ShowCommand());
        commands.put("info", new InfoCommand());
        commands.put("update", new UpdateCommand(scannerStack));
        commands.put("remove_by_id", new RemoveByIdCommand());
        commands.put("clear", new ClearCommand());
        commands.put("remove_at_index", new RemoveByIndexCommand());
        commands.put("remove_last", new RemoveLastCommand());
        commands.put("shuffle", new ShuffleCommand());
        commands.put("execute_script", new ExecuteScriptCommand(commands, scannerStack, fileSet));
        commands.put("countGreaterThanNumberOfParticipants", new CountGreaterThanNumberOfParticipants());
        commands.put("filter_greater_than_label", new FilterGreaterThanLabel(scannerStack));
        commands.put("printFieldAscendingNumberOfParticipants", new PrintAscendingListNumOfParticipants());

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
                        throw new RuntimeException();
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
//модуль Dispatcher для коннекта с сервером!


        try {

            byte arr[] = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
            int len = arr.length;

            Socket sock;
            OutputStream os;
            InputStream is;
            InetAddress host;
            int port;
            port = 6789;
            host = InetAddress.getLocalHost(); //пока чо
            sock = new Socket(host, port);

            os = sock.getOutputStream();
            os.write(arr);
            is = sock.getInputStream();
            is.read(arr);

            for (byte j : arr) {
                System.out.println(j);
            }

        } catch (UnknownHostException e) {
            System.out.println("где локалхост вася");
        } catch (IOException e) {
            System.out.println("Вася все хуйня");
            e.printStackTrace();
        }

    }
}