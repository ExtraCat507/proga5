package org.xtracat.auth;

import org.xtracat.client.util.MyDispatcher;
import org.xtracat.client.util.Request;
import org.xtracat.client.util.Response;
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

    public UserRecord auth() {
        System.out.println("Введите команду:");
        System.out.print("\n" +
                "+------+------+\n" +
                "| /log | /reg |\n" +
                "+------+------+\n" +
                "\n");

        String inp;
        UserRecord resulted;
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


    private UserRecord login() {
        System.out.println("Инициирована аутентификация...");
        UserBuilder userBuilder = new UserBuilder();
        System.out.println("Введите логин:");
        String userLogin = scanner.nextLine().trim();
        userBuilder.setLogin(userLogin);
        System.out.println("Введите пароле:");
        String userPassword = scanner.nextLine().trim();

        userBuilder.setPassword(userPassword);
        UserRecord user = userBuilder.build();
        Request authRequest = new Request("auth",null,user);
        Response authResponse = dispatcher.send(authRequest);

        if ((boolean) authResponse.getData()) {
            System.out.println("Успешный вход");
            return user;
        }
        System.out.println("Неверный логин или пароль");
        return null;

    }

    private UserRecord register() {
        System.out.println("Добро пожаловать!");
        UserBuilder userBuilder = new UserBuilder();
        System.out.println("Введите логин:");
        String userLogin = scanner.nextLine().trim();
        userBuilder.setLogin(userLogin);
        System.out.println("Введите пароле:");
        String userPassword = scanner.nextLine().trim();

        userBuilder.setPassword(userPassword);
        UserRecord user = userBuilder.build();
        Request authRequest = new Request("register",null , user);
        Response regResponse = dispatcher.send(authRequest);

        if ((boolean) regResponse.getData()) {
            System.out.println("Успешная регистрация");
            return user;
        }
        System.out.println("Что-то пошло не так..");
        return null;


    }


}
