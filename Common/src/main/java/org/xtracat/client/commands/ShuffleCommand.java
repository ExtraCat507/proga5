package org.xtracat.client.commands;

import org.xtracat.server.CollectionManager;

public class ShuffleCommand implements Command {
    CollectionManager cm;

    public ShuffleCommand() {

    }

    @Override
    public void execute(int mode, String[] args) {
        cm.shuffle();
        System.out.println("Перемешано");
    }

    @Override
    public String descr() {
        return "shuffle - перемешать лист";
    }
}
