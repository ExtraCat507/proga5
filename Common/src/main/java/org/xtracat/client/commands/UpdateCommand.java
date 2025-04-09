package org.xtracat.client.commands;

import org.xtracat.client.util.AdvancedScanner;
import org.xtracat.server.CollectionManager;

import java.util.List;
import java.util.Scanner;

public class UpdateCommand implements Command {
    CollectionManager cm;
    AdvancedScanner advSc;
    List<Scanner> scannerStack;

    public UpdateCommand(List<Scanner> scannerStack) {
        this.scannerStack = scannerStack;
    }


    @Override
    public void execute(int mode, String[] args) {
        this.advSc = new AdvancedScanner(scannerStack.get(scannerStack.size() - 1));
        try {
            long id = Long.parseLong(args[0]);
            if (cm.findById(id) == -1) {
                System.out.println("Не существует id");
                return;
            }
            int callback = cm.changeById(id, advSc.createMusicBand(cm));
            if (callback == 0) {
                System.out.println("Элемент успешно изменен");
            }
        } catch (NumberFormatException e) {
            System.out.println("Неправильный формат аргумента");
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Нет аргумента");
        }
    }

    @Override
    public String descr() {
        return "";
    }
}
