package org.xtracat.server;

import java.util.Scanner;

public class ServerConsole {

    private static volatile boolean running = true; // used for graceful shutdown

    public static void runConsole(String filename, CollectionManager cm) {
        Scanner scanner = new Scanner(System.in);

        while (running) {
            if (scanner.hasNextLine()) {
                String command = scanner.nextLine().trim();
                processCommand(command, filename, cm);
            }
        }
    }

    private static void processCommand(String command,String filename,CollectionManager cm) {
        switch (command.toLowerCase()) {
            case "exit":
                System.out.println("Shutting down server...");
                running = false;


                System.exit(0);
                break;

            case "save":
                System.out.println("Saving current state...");
                // Call your save logic here (e.g., save database, write file, etc.)
                break;

            default:
                System.out.println("Unknown command: " + command);
        }
    }

    public static boolean isRunning() {
        return running;
    }
}
