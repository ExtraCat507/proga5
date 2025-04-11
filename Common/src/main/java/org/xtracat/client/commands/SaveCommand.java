package org.xtracat.client.commands;

import org.xtracat.client.util.Request;
import org.xtracat.client.util.Response;
import org.xtracat.server.CollectionManager;
import org.xtracat.storage.Serializer;

public class SaveCommand implements Command {
    private final Serializer sr;
    private final String filename;
    CollectionManager cm;

    public SaveCommand(String filename, CollectionManager collectionManager) {
        this.sr = new Serializer();
        this.filename = filename;
        this.cm = collectionManager;
    }

    @Override
    public void execute(int mode, String[] args) {
        int callback = sr.save(this.filename, cm);
        if (callback == 0) {
            System.out.println("Готово\n");
        }
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
        return "save - Сохраняет коллекцию в формате xml";
    }
}
