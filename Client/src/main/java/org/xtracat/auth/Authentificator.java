package org.xtracat.auth;

import org.xtracat.client.util.MyDispatcher;
import org.xtracat.client.util.Request;
import org.xtracat.client.util.Response;
import org.xtracat.usershit.User;
import org.xtracat.usershit.UserBuilder;
import org.xtracat.usershit.UserRecord;

import java.util.Scanner;

public class Authentificator {
    private final Scanner scanner;
    private final MyDispatcher dispatcher;

    public Authentificator(Scanner sc, MyDispatcher dispatcher) {
        this.scanner = sc;
        this.dispatcher = dispatcher;
    }

    public User auth() {
        System.out.println("Введите команду:");
        System.out.print("\n" +
                "+------+------+\n" +
                "| /log | /reg |\n" +
                "+------+------+\n" +
                "\n");

        String inp;
        User resulted;
        while (true) {
            inp = scanner.nextLine().trim();
            switch (inp) {
                case "/log":
                    resulted = login();
                    if(resulted == null){
                        System.out.println("Введите команду:");
                        System.out.print("\n" +
                                "+------+------+\n" +
                                "| /log | /reg |\n" +
                                "+------+------+\n" +
                                "\n");
                        continue;
                    }
                    return resulted;
                case "/reg":
                    resulted = register();
                    if(resulted == null){
                        System.out.println("Введите команду:");
                        System.out.print("\n" +
                                "+------+------+\n" +
                                "| /log | /reg |\n" +
                                "+------+------+\n" +
                                "\n");
                        continue;
                    }
                    return resulted;
                case "exit":
                    System.out.println("Exiting...");
                    System.exit(0);
                default:
                    System.out.println("Wrong input!");

            }
        }

    }


    private User login() {
        System.out.println("Инициирована аутентификация...");
        System.out.println("Введите логин:");
        String userLogin = scanner.nextLine().trim();
        System.out.println("Введите пароле:");
        String userPassword = scanner.nextLine().trim();

        User user = new User(userLogin,userPassword);
        Request authRequest = new Request("auth",null,user);
        Response authResponse = dispatcher.send(authRequest);

        System.out.println(authResponse.getMessage());
        return (User) authResponse.getData();

    }

    private User register() {
        System.out.println("Добро пожаловать!");
        System.out.println("Введите логин:");
        String userLogin = scanner.nextLine().trim();
        System.out.println("Введите пароле:");
        String userPassword = scanner.nextLine().trim();

        User user = new User(userLogin,userPassword);
        Request authRequest = new Request("register",null , user);
        Response regResponse = dispatcher.send(authRequest);

        System.out.println(regResponse.getMessage());
        return (User) regResponse.getData();


    }


}
