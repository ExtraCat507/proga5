package org.xtracat.client.util;

import java.io.Serializable;

public class Response implements Serializable {
    String message;
    Object data;

    public Response(String message, Object data){
        this.message = message;
        this.data = data;
    }

    public Response(String message){
        this(message,null);
    }

    public Response(){
        this("Пустой ответ от сервера",null);
    }

    public String getMessage(){
        return message;
    }

    public Object getData() {
        return data;
    }

    @Override
    public String toString(){
        return "Response: " + getMessage() + "; " + getData() + "\n";
    }
}
