package org.xtracat;

import org.xtracat.client.util.MyDispatcher;
import org.xtracat.client.util.Request;
import org.xtracat.client.util.Response;

public class ServerDispatcher implements MyDispatcher {
    public Response send(Request request){
        return CommandTransferer.getResponse(request);
    }
}
