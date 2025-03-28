package org.xtracat.client.commands;

import org.xtracat.server.CollectionManager;

public class ClearCommand implements Command {
    CollectionManager cm;
    //Scanner sc ;

    public ClearCommand(String filename, CollectionManager collectionManager) {
        this.cm = collectionManager;
        // this.sc = new Scanner(System.in);
    }

    @Override
    public void execute(int mode, String[] args) {
        //System.out.println("Подтвердите удаление коллекции(y/n)");
//        //String response = sc.next();
//        if(response.equals("y")){
//            cm.clearCollection();
//            System.out.println("Удаление успешно");
//        }
        cm.clearCollection();

    }

    @Override
    public String descr() {
        return "clear - удаление всей коллекции";
    }
}
