package org.xtracat.serverCommands;

import org.xtracat.client.util.Request;
import org.xtracat.client.util.Response;

public interface ServerCommand {
    public Response execute(Request request);
}
