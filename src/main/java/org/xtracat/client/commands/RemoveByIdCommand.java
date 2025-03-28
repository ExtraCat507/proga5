package org.xtracat.client.commands;

import org.xtracat.server.CollectionManager;

public class RemoveByIdCommand implements Command {
    CollectionManager cm;

    public RemoveByIdCommand(String filename, CollectionManager collectionManager) {
        this.cm = collectionManager;
    }

    @Override
    public void execute(int mode, String[] args) {
        try {
            long id = Long.parseLong(args[0]);
            if (cm.findById(id) == -1) {
                System.out.println("Не существует id");
                return;
            }
            int callback = cm.removeById(id);
            if (callback == 0) {
                System.out.println("Элемент успешно удален");
            }
        } catch (NumberFormatException e) {
            System.out.println("Неправильный формат аргумента");
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Нет аргумента");
        }
    }


    @Override
    public String descr() {
        return "remove_by_id - удаляет элемент коллекции по id";
    }
}
