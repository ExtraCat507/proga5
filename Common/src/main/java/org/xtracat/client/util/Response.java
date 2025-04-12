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
        new Response(message,null);
    }

    public Response(){
        new Response("Пустой ответ от сервера",null);
    }

    public String getMessage(){
        return message;
    }

    public Object getData() {
        return data;
    }
}
