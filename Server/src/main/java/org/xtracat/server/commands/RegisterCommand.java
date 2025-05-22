package org.xtracat.server.commands;

import org.xtracat.client.util.Request;
import org.xtracat.client.util.Response;
import org.xtracat.database.DataBaseManager;
import org.xtracat.security.HashManager;
import org.xtracat.usershit.PasswordRecord;
import org.xtracat.usershit.User;

public class RegisterCommand implements ServerCommand{
    @Override
    public Response execute(Request request) {
        User user = request.getUser();
        PasswordRecord passwordRecord = HashManager.hash(user.password());
        try{
            DataBaseManager.register(user.login(),passwordRecord);
            return new Response("Регистрация успешнв",user);
        }catch (Exception e){
            return new Response("Ошибка при регистрации",null);
        }
    }
}
