package org.xtracat.client.util;

import java.io.Serializable;

public class Request implements Serializable {
    String command;
    Object content;
    public Request(String command, Object content){
        this.content = content;
        this.command = command;
    }
}
