package org.xtracat.client.commands;

import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class ExecuteScriptCommand implements Command {
    Map<String, Command> commands;
    List<Scanner> scannerStack;
    List<File> fileSet;


    public ExecuteScriptCommand(Map<String, Command> commands, List<Scanner> scannerStack, List<File> fileSet) {
        this.commands = commands;
        this.scannerStack = scannerStack;
        this.fileSet = fileSet;
    }

    @Override
    public void execute(int mode, String[] args) {

        try (InputStreamReader reader = new InputStreamReader(new FileInputStream(args[0]))) {
            Scanner sc = new Scanner(reader);
            File file = new File(args[0]);

            if (fileSet.contains(file)) {
                throw new StackOverflowError("Обнаружена рекурсия, это против правил");
            }

            fileSet.add(file);
            scannerStack.add(sc); // добавили сканер в стек
            sc.hasNext();
            System.out.println("Начинаю обработку команд в файле " + args[0] + ":\n-------------------");

        } catch (FileNotFoundException e) {
            System.out.println("Нет такого файла!!");
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Нет аргумента");
        } catch (StackOverflowError e) {
            System.out.println(e.getMessage() + "\nФайл вызвавший рекурсию: " + args[0]);
            scannerStack.subList(1, scannerStack.size()).clear();
            fileSet.clear();
            // System.out.println(scannerStack.size());
        }
    }


    @Override
    public String descr() {
        return "execute_script file_name - исполнить файл с командами";
    }
}
