package org.xtracat.serverCommands;

import org.xtracat.client.util.Request;
import org.xtracat.client.util.Response;
import org.xtracat.server.CollectionManager;

public class ServerPrintAscendingListNumOfParticipants implements ServerCommand {
    private final CollectionManager cm;

    public ServerPrintAscendingListNumOfParticipants(CollectionManager collectionManager) {
        this.cm = collectionManager;
    }

    @Override
    public Response execute(Request request) {
        try {
            long[] result = cm.printFieldAscendingNumberOfParticipants();
            StringBuilder sb = new StringBuilder("[ ");
            for (long num : result) {
                sb.append(num).append(" ");
            }
            sb.append("]");
            return new Response("Значения числа участников по возрастанию: " + sb.toString(), result);
        } catch (Exception e) {
            return new Response("Ошибка при выполнении команды: " + e.getMessage());
        }
    }
}
