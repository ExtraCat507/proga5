package org.xtracat.server.commands;

import org.xtracat.client.util.Request;
import org.xtracat.client.util.Response;
import org.xtracat.dao.DatabaseManager;
import org.xtracat.dao.SingletonDAO;
import org.xtracat.security.HashManager;
import org.xtracat.usershit.PasswordRecord;
import org.xtracat.usershit.User;

import java.util.Arrays;

public class AuthCommand implements ServerCommand {
    @Override
    public Response execute(Request request) {

        User user = request.getUser();
        DatabaseManager dao = SingletonDAO.getDao();
        PasswordRecord proof = dao.getProof(user.login());
        System.out.println(proof.salt());
        PasswordRecord passwordRecord = HashManager.hashWithSalt(user.password(), proof.salt());
        if (proof.salt()!=null && Arrays.equals(proof.hash(), passwordRecord.hash())) {
            return new Response("Успешный вход", user);
        }
        return new Response("Неверный логин или пароль", null);
    }
}
