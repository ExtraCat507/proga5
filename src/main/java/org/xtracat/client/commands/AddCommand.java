package org.xtracat.client.commands;

import org.xtracat.client.util.AdvancedScanner;
import org.xtracat.datatypes.MusicBand;
import org.xtracat.server.CollectionManager;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Команда добавления объекта в коллекцию
 */
public class AddCommand implements Command { // Add Command

    CollectionManager cm;
    AdvancedScanner advSc;
    List<Scanner> scannerStack;

    public AddCommand(String filename, CollectionManager collectionManager, List<Scanner> scannerStack) {
        //this.advSc = new AdvancedScanner();
        this.cm = collectionManager;
        this.scannerStack = scannerStack;
    }

    /**
     * Создает новый экземпляр класса MusicBand и помещает его в коллекцию
     */
    @Override
    public void execute(int mode, String[] args) {
        MusicBand band = null;
        this.advSc = new AdvancedScanner(scannerStack.get(scannerStack.size() - 1));
        if (mode == 1) {
            try {
                band = advSc.createMusicBandInScript(cm);
            } catch (NoSuchElementException | IllegalArgumentException e) {
                System.out.println("Данные были введены неверно, ничего не сохранено");
                return;
            }
            System.out.println(band);

        } else {
            band = advSc.createMusicBand(cm); // creating band with user interaction
        }

        if (band == null) {
            return;
        }

        cm.add(band);   //Collection manager saves band on the server
        System.out.println("Сохранил)");
    }

    @Override
    public String descr() {
        return "add - добавляет новый элемент в коллекцию";
    }

}
