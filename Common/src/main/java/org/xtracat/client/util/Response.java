package org.xtracat.client.util;

import java.io.Serializable;

public class Response implements Serializable {
    String message;
    Object data;
    public String getMessage(){
        return message;
    }

    public Object getData() {
        return data;
    }
}
