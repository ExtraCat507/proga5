package org.xtracat.client.commands;

import org.xtracat.server.CollectionManager;

public class CountGreaterThanNumberOfParticipants implements Command {
    CollectionManager cm;

    public CountGreaterThanNumberOfParticipants() {

    }

    @Override
    public void execute(int mode, String[] args) {
        try {
            int number = Integer.parseInt(args[0]);
            System.out.println("Кол-во эл-тов, где NumberOfParticipants больше чем " + number + " : " + cm.countGreaterThanNumberOfParticipants(number));
        } catch (NumberFormatException e) {
            System.out.println("Неправильный формат аргумента");
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Нет аргумента");
        }
    }

    @Override
    public String descr() {
        return "countGreaterThanNumberOfParticipants num - возвращает число элементов, где NumberOfParticipants больше заданного ";
    }
}
