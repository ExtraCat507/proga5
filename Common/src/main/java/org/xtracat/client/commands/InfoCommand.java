package org.xtracat.client.commands;

import org.xtracat.server.CollectionManager;

public class InfoCommand implements Command {
    CollectionManager cm;

    public InfoCommand() {

    }

    @Override
    public void execute(int mode, String[] args) {
        System.out.println(cm.getBandsCollection().toString());
    }

    @Override
    public String descr() {
        return "info - вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов)\n";
    }
}
