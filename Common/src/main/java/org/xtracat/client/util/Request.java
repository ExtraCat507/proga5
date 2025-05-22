package org.xtracat.client.util;

import org.xtracat.usershit.User;
import org.xtracat.usershit.UserRecord;

import java.io.Serializable;

public class Request implements Serializable {
    String command;
    User user;
    Object content;

    public Request(String command, Object content, User user){
        this.content = content;
        this.command = command;
        this.user = user;
    }

    public Request(String command, Object content) {
        new Request(command,content,null);
    }

    @Override
    public String toString() {
        return "Request : " + command + "; " + content + ". From user: " + user;
    }

    public Object getContent() {
        return this.content;
    }

    public String getCommand() {
        return command;
    }

    public User getUser(){
        return user;
    }

}
