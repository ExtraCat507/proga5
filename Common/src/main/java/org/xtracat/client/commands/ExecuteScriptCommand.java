package org.xtracat.client.commands;

import org.xtracat.client.util.MyDispatcher;
import org.xtracat.client.util.Request;
import org.xtracat.client.util.Response;
import org.xtracat.usershit.User;

import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
//КЛИЕНТСКАЯ КОМАНДА
public class ExecuteScriptCommand implements Command {
    Map<String, Command> commands;
    List<Scanner> scannerStack;
    List<File> fileSet;


    public ExecuteScriptCommand(MyDispatcher dispatcher, Map<String, Command> commands, List<Scanner> scannerStack, List<File> fileSet) {
        this.commands = commands;
        this.scannerStack = scannerStack;
        this.fileSet = fileSet;
    }

    @Override
    public void execute(int mode, User user, String[] args) {

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
    public void prepare() {

    }

    @Override
    public Request buildRequest() {
        return null;
    }

    @Override
    public void processResponse(Response response) {

    }


    @Override
    public String descr() {
        return "execute_script file_name - исполнить файл с командами";
    }
}
