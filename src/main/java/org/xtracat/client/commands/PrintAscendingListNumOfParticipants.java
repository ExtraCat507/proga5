package org.xtracat.client.commands;

import org.xtracat.server.CollectionManager;

public class PrintAscendingListNumOfParticipants implements Command {
    CollectionManager cm;

    public PrintAscendingListNumOfParticipants(String filename, CollectionManager collectionManager) {
        this.cm = collectionManager;
    }

    @Override
    public void execute(int mode, String[] args) {
        long[] result = cm.printFieldAscendingNumberOfParticipants();
        System.out.print("[ ");
        for (long i : result) {
            System.out.print(i + " ");
        }
        System.out.print("]\n");
    }

    @Override
    public String descr() {
        return "printFieldAscendingNumberOfParticipants - вывеcти значения всех numOfPart в порядке неубывания\n";
    }
}
