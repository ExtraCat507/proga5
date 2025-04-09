package org.xtracat.client.commands;

import org.xtracat.server.CollectionManager;

public class ClearCommand implements Command {
    CollectionManager cm;

    public ClearCommand() {
    }

    @Override
    public void execute(int mode, String[] args) {
        cm.clearCollection();

    }

    @Override
    public String descr() {
        return "clear - удаление всей коллекции";
    }
}
