package org.xtracat.client.commands;

import org.xtracat.client.util.*;
import org.xtracat.datatypes.MusicBand;
import org.xtracat.usershit.User;

import java.util.List;
import java.util.Scanner;

public class UpdateCommand implements Command {

    private final MyDispatcher dispatcher;
    private final List<Scanner> scannerStack;
    private long id;
    private MusicBand musicBand;
    private int mode; // режим, передаваемый в execute

    public UpdateCommand(MyDispatcher dispatcher, List<Scanner> scannerStack) {
        this.dispatcher = dispatcher;
        this.scannerStack = scannerStack;
    }

    @Override
    public void prepare() {
        AdvancedScanner advSc = new AdvancedScanner(scannerStack.get(scannerStack.size() - 1));
        // Выбор метода ввода в зависимости от режима
        if (mode == 1) {
            musicBand = advSc.createMusicBandInScript();
        } else {
            musicBand = advSc.createMusicBand();
        }
    }

    @Override
    public Request buildRequest() {
        // Формируем payload как массив: первый элемент - id, второй - объект MusicBand
        Object[] payload = new Object[]{id, musicBand};
        return new Request("update", payload);
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
    public void execute(int mode, User user, String[] args) {
        this.mode = mode;
        try {
            id = Long.parseLong(args[0]);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Нет аргумента");
            return;
        } catch (NumberFormatException e) {
            System.out.println("Неправильный формат аргумента");
            return;
        }
        prepare();
        if (musicBand == null) {
            return;
        }
        Request request = buildRequest();
        request.setUser(user);
        Response response = dispatcher.send(request);
        processResponse(response);
    }

    @Override
    public String descr() {
        return "update id - обновить элемент коллекции по id";
    }
}
