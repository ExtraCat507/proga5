package org.xtracat.client.commands;

import java.util.Map;

public class HelpCommand implements Command {
    private final Map<String, Command> commands;

    public HelpCommand(Map<String, Command> commands) {
        this.commands = commands;
    }

    @Override
    public void execute(int mode, String[] args) {
        for (Command c : commands.values()) {
            System.out.println(c.descr());
        }
    }

    @Override
    public String descr() {
        return "";
    }
}
