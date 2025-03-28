package org.xtracat.client.commands;

import org.xtracat.client.util.AdvancedScanner;
import org.xtracat.datatypes.Label;
import org.xtracat.datatypes.MusicBand;
import org.xtracat.server.CollectionManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FilterGreaterThanLabel implements Command {
    CollectionManager cm;
    AdvancedScanner advSc;
    List<Scanner> scannerStack;

    public FilterGreaterThanLabel(String filename, CollectionManager collectionManager, List<Scanner> scannerStack) {
        this.cm = collectionManager;
        this.scannerStack = scannerStack;

    }

    @Override
    public void execute(int mode, String[] args) {
        this.advSc = new AdvancedScanner(scannerStack.get(scannerStack.size() - 1));
        Label comparedLabel;
        if (mode == 1) {
            comparedLabel = advSc.enterLabelInScript(cm);
        } else {
            comparedLabel = advSc.enterLabel();
        }

        if (comparedLabel == null) {
            System.out.println("Не получится сравнить: введенный Label - null");
            return;
        }
        ArrayList<MusicBand> result = cm.filterGreaterThanLabel(comparedLabel);
        System.out.println("Вот все группы с лейблом больше этого:");
        System.out.println("________________");
        for (MusicBand band : result) {
            if (band == null) {
                break;
            }
            System.out.println(band);
        }
    }

    @Override
    public String descr() {
        return "filterGreaterThanLabel {label} - вывести все эл-ты где label больше заданного";
    }
}
