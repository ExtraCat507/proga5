package org.xtracat.server.commands;

import org.xtracat.client.util.Request;
import org.xtracat.client.util.Response;
import org.xtracat.database.DataBaseManager;
import org.xtracat.security.HashManager;
import org.xtracat.usershit.PasswordRecord;
import org.xtracat.usershit.User;

import java.util.Arrays;

public class AuthCommand implements ServerCommand{
    @Override
    public Response execute(Request request) {

        User user = request.getUser();
        PasswordRecord passwordRecord = HashManager.hash(user.password());

        PasswordRecord proof = DataBaseManager.getProof();

        if (Arrays.equals(proof.hash(), passwordRecord.hash())) {
            return new Response("Успешный вход",user);
        }
        return new Response("Неверный логин или пароль",null);
    }
}
