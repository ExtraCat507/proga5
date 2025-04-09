package org.xtracat.client.commands;

public class LoadCommand implements Command {

    @Override
    public void execute(int mode, String[] args) {
        //cm.load(args[0]);
    }

    @Override
    public String descr() {
        return "load - загружает структуру из файла";
    }
}
