package org.xtracat.server.commands;

import org.xtracat.client.util.Request;
import org.xtracat.client.util.Response;
import org.xtracat.datatypes.MusicBand;
import org.xtracat.CollectionManager;

public class ServerAddCommand implements ServerCommand{
    CollectionManager cm;
    public ServerAddCommand(CollectionManager cm){
        this.cm = cm;
    };

    @Override
    public Response execute(Request request) {
        try {
            MusicBand band = (MusicBand) request.getContent();
            band.setAuthor(request.getUser().login());
            cm.add(band);
            return new Response("Успешное добавление");
        }catch (Exception e){
            return new Response("Ошибка при добавлении");
        }
    }
}
