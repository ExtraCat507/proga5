package org.xtracat.client.commands;

import org.xtracat.server.CollectionManager;

public class RemoveLastCommand implements Command {
    CollectionManager cm;

    public RemoveLastCommand(String filename, CollectionManager collectionManager) {
        this.cm = collectionManager;
    }

    @Override
    public void execute(int mode, String[] args) {
        int callback = cm.removeLast();
        if (callback == 0) {
            System.out.println("Удаление успешно");
        } else if (callback == -1) {
            System.out.println("Коллекция уже пуста");
        }
    }

    @Override
    public String descr() {
        return "remove_last - удалить последний эл-т";
    }
}
