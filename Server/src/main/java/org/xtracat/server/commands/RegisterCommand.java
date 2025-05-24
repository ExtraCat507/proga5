package org.xtracat.server.commands;

import org.xtracat.client.util.Request;
import org.xtracat.client.util.Response;
import org.xtracat.dao.DatabaseManager;
import org.xtracat.dao.SingletonDAO;
import org.xtracat.security.HashManager;
import org.xtracat.usershit.PasswordRecord;
import org.xtracat.usershit.User;

import java.sql.SQLException;

public class RegisterCommand implements ServerCommand{
    @Override
    public Response execute(Request request) {
        User user = request.getUser();
        PasswordRecord passwordRecord = HashManager.hash(user.password());
        DatabaseManager dao = SingletonDAO.getDao();
        try{
            boolean result = dao.register(user.login(),passwordRecord);
            if(result) {
                return new Response("Регистрация успешнв", user);
            }
            return new Response("Логин занят", null);
        }catch (SQLException e){
            return new Response("Ошибка при регистрации",null);
        }
    }
}
