package org.xtracat.client.util;

import org.xtracat.usershit.UserRecord;

import java.io.Serializable;

public class Request implements Serializable {
    String command;
    UserRecord userRecord;
    Object content;

    public Request(String command, Object content, UserRecord userRecord){
        this.content = content;
        this.command = command;
        this.userRecord = userRecord;
    }

    public Request(String command, Object content) {
        new Request(command,content,null);
    }

    @Override
    public String toString() {
        return "Request : " + command + "; " + content + ". From user: " + userRecord;
    }

    public Object getContent() {
        return this.content;
    }

    public String getCommand() {
        return command;
    }

}
