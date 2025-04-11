package org.xtracat.client.commands;


import org.xtracat.client.util.Request;
import org.xtracat.client.util.Response;

public class LoadCommand implements Command {

    @Override
    public void execute(int mode, String[] args) {
        //cm.load(args[0]);
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
        return "load - загружает структуру из файла";
    }
}
