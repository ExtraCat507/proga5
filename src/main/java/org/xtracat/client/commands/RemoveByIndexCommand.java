package org.xtracat.client.commands;

import org.xtracat.server.CollectionManager;

public class RemoveByIndexCommand implements Command {
    CollectionManager cm;

    public RemoveByIndexCommand(String filename, CollectionManager collectionManager) {
        this.cm = collectionManager;
    }

    @Override
    public void execute(int mode, String[] args) {
        try {
            int id = Integer.parseInt(args[0]);
            int callback = cm.removeByIndex(id);
            if (callback == 0) {
                System.out.println("Элемент успешно удален");
            } else if (callback == -1) {
                System.out.println("Индекс аут оф рэндж");
            }
        } catch (NumberFormatException e) {
            System.out.println("Неправильный формат аргумента");
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Нет аргумента");
        }
    }

    @Override
    public String descr() {
        return "remove_at_index - удалить по индексу листа";
    }
}
