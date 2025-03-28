package org.xtracat.client.commands;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Scanner;

public class ExecuteScriptCommand2 implements Command {
    Map<String, Command> commands;
    ArrayList<File> fileStack;
    boolean timeToGo;


    public ExecuteScriptCommand2(Map<String, Command> commands) {
        this.commands = commands;
        this.fileStack = new ArrayList<>();
        this.timeToGo = false;
    }

    @Override
    public void execute(int mode, String[] args) {

        try (InputStreamReader reader = new InputStreamReader(new FileInputStream(args[0]))) {
            Scanner sc = new Scanner(reader);

            File file = new File(args[0]);
            for (File x : fileStack) {
                if (x.equals(file)) {
                    throw new StackOverflowError("Обнаружена рекурсия, это против правил");
                }
            }
            fileStack.add(file);
            System.out.println("Начинаю обработку команд в файле" + args[0] + ":\n-------------------");
            while (sc.hasNext()) {
                String line = sc.nextLine();
                System.out.println("Команда: " + line);
                String[] tokens = line.split(" ");
                Command command = commands.get(tokens[0]);
                if (command == null) {
                    System.out.println("Not a command");
                    continue;
                }
                command.execute(1, Arrays.copyOfRange(tokens, 1, tokens.length));
                if (timeToGo) {
                    fileStack.remove(fileStack.size() - 1);
                    //System.out.println("продолжаем выходить");
                    if (fileStack.isEmpty()) {
                        timeToGo = false;
                        //System.out.println("бебебе закончили");
                    }

                    return;
                }
            }
            fileStack.remove(fileStack.size() - 1);


        } catch (FileNotFoundException e) {
            System.out.println("Нет такого файла!!");
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Нет аргумента");
        } catch (StackOverflowError e) {
            System.out.println(e.getMessage() + "\nФайл вызвавший рекурсию: " + args[0]);
            timeToGo = true;
            //System.out.println("выход init");
        }
    }


    @Override
    public String descr() {
        return "execute_script file_name - исполнить файл с командами";
    }
}
