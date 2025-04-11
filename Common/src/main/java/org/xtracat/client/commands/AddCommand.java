package org.xtracat.client.commands;

import org.xtracat.client.util.AdvancedScanner;
import org.xtracat.client.util.Dispatcher;
import org.xtracat.client.util.Request;
import org.xtracat.client.util.Response;
import org.xtracat.datatypes.MusicBand;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class AddCommand implements Command {

    private final List<Scanner> scannerStack;
    private final Dispatcher dispatcher;
    private MusicBand musicBand;
    private int mode;

    public AddCommand( Dispatcher dispatcher,List<Scanner> scannerStack) {
        this.scannerStack = scannerStack;
        this.dispatcher = dispatcher;
    }

    @Override
    public void prepare() {
        AdvancedScanner advSc = new AdvancedScanner(scannerStack.get(scannerStack.size() - 1));
        try {
            if (mode == 1) {
                musicBand = advSc.createMusicBandInScript();
            } else {
                musicBand = advSc.createMusicBand();
            }
        } catch (NoSuchElementException | IllegalArgumentException e) {
            System.out.println("Данные были введены неверно, объект не будет создан.");
            musicBand = null;
        }
    }

    @Override
    public Request buildRequest() {
        if (musicBand == null) {
            return null;
        }
        return new Request("add", musicBand);
    }

    @Override
    public void processResponse(Response response) {
        if (response != null) {
            System.out.println(response.getMessage());
        } else {
            System.out.println("Нет ответа от сервера.");
        }
    }

    @Override
    public void execute(int mode, String[] args) {
        this.mode = mode;
        prepare();
        Request request = buildRequest();
        if (request != null) {
            Response response = dispatcher.send(request);
            processResponse(response);
        }
    }

    @Override
    public String descr() {
        return "add - добавляет новый элемент в коллекцию.";
    }
}
