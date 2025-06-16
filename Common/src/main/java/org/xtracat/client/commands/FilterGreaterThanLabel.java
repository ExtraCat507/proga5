package org.xtracat.client.commands;

import org.xtracat.client.util.*;
import org.xtracat.datatypes.MusicLabel;
import org.xtracat.datatypes.MusicBand;
import org.xtracat.usershit.User;

import java.util.List;
import java.util.Scanner;

public class FilterGreaterThanLabel implements Command {

    private final MyDispatcher dispatcher;
    private final List<Scanner> scannerStack;
    private MusicLabel comparedMusicLabel;
    private int mode;

    public FilterGreaterThanLabel(MyDispatcher dispatcher, List<Scanner> scannerStack) {
        this.dispatcher = dispatcher;
        this.scannerStack = scannerStack;
    }

    @Override
    public void prepare() {
        AdvancedScanner advSc = new AdvancedScanner(scannerStack.get(scannerStack.size() - 1));
        if (mode == 1) {
            comparedMusicLabel = advSc.enterLabelInScript();
        } else {
            comparedMusicLabel = advSc.enterLabel();
        }
        if (comparedMusicLabel == null) {
            System.out.println("Не получится сравнить: введенный Label равен null");
        }
    }

    @Override
    public Request buildRequest() {
        return new Request("filterGreaterThanLabel", comparedMusicLabel);
    }

    @Override
    public void processResponse(Response response) {
        if (response == null) {
            System.out.println("Нет ответа от сервера.");
            return;
        }

        Object data = response.getData();
        if (data instanceof List) {
            List<MusicBand> bandList = (List<MusicBand>) data;
            if (bandList.isEmpty()) {
                System.out.println("Элементов больше заданного Label не найдено.");
            } else {
                System.out.println("****************************");
                for (MusicBand band : bandList) {
                    System.out.println(band);
                }
                System.out.println("****************************");
            }
        } else {
            System.out.println(response.getMessage());
        }
    }

    @Override
    public void execute(int mode, User user, String[] args) {
        this.mode = mode;
        prepare();
        if (comparedMusicLabel == null) {
            return;
        }
        Request request = buildRequest();
        request.setUser(user);
        Response response = dispatcher.send(request);
        processResponse(response);
    }

    @Override
    public String descr() {
        return "filterGreaterThanLabel {label} - выводит все элементы, где label больше заданного";
    }
}
