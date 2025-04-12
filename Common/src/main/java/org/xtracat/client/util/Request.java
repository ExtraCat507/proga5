package org.xtracat.client.util;

import java.io.Serializable;

public class Request implements Serializable {
    String command;
    Object content;

    public Request(String command, Object content) {
        this.content = content;
        this.command = command;
    }

    @Override
    public String toString() {
        return "Request : " + command + "; " + content;
    }

    public Object getContent() {
        return this.content;
    }

    public String getCommand() {
        return command;
    }

}
