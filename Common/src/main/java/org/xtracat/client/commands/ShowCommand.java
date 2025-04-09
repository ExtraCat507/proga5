package org.xtracat.client.commands;

import org.xtracat.datatypes.MusicBand;
import org.xtracat.server.CollectionManager;

import java.util.LinkedList;

public class ShowCommand implements Command {
    CollectionManager cm;

    public ShowCommand() {

    }

    @Override
    public void execute(int mode, String[] args) {
        System.out.println("****************************");
        System.out.println("\n________________\n");
        LinkedList<MusicBand> bandLinkedList = cm.getList();
        for (MusicBand band : bandLinkedList) {
            System.out.println(band.toString());
        }
        System.out.println("****************************");
    }

    @Override
    public String descr() {
        return "show - выводит все элементы коллекции";
    }
}
