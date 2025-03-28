package org.xtracat.client.commands;

public interface Command { // Abstract Command
    void execute(int mode, String[] args);

    String descr();
}