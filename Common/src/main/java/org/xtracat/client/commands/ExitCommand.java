package org.xtracat.client.commands;

public class ExitCommand implements Command {
    @Override
    public void execute(int mode, String[] args) {
        System.out.println("Работа приложения завершена, ничего не сохраняю )))");
        System.exit(0);
    }

    @Override
    public String descr() {
        return "exit - завершает работу прилоэения без сохранения";
    }
}
