package org.xtracat;

import org.slf4j.Logger;
import org.xtracat.client.util.Request;
import org.xtracat.client.util.Response;
import org.xtracat.singleton.SingletonLogger;

import java.io.*;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;


public class ChannelManager {

    private final Logger logger = SingletonLogger.getLogger();

    public ChannelManager() {
    }


    public Request readRequest(SocketChannel channel) {

        ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
        try {
            int bytesRead = channel.read(buffer);
            if (bytesRead == -1) {
                logger.info("Client closed connection");
                channel.close();
                return null;
            }
        } catch (SocketException e) {
            logger.error("Socket exception!", e);
        } catch (IOException e) {
            logger.error("Error in Read", e);
        }

        Request request = parseRequest(buffer);
        if (request == null) {
            logger.error("Invalid Request");
            return null; // todo: wtf ??
        }
        return request;
    }

    private Request parseRequest(ByteBuffer buffer) {
        buffer.flip();
        byte[] arr = new byte[buffer.remaining()];
        buffer.get(arr);
        buffer.clear();
        try (ByteArrayInputStream bis = new ByteArrayInputStream(arr);
             ObjectInputStream ois = new ObjectInputStream(bis)) {
            Object obj = ois.readObject();
            if (obj instanceof Request) {
                return (Request) obj;
            } else {
                logger.error("Unsupported type - not Request");
                return null;
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    private byte[] serializeResponse(Response response) {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(response);
            return bos.toByteArray();
        } catch (IOException e) {
            logger.error("Something went wrong on serialization of Response" + e);
        }
        return new byte[0];
    }

    public void writeResponse(SocketChannel channel, Response response) {
        try {
            ByteBuffer buffer = ByteBuffer.allocateDirect(1_048_576);
            if (response == null) {
                logger.error("Empty response - null buffer. Nothing was sent to client");
                return;
            }
            buffer.put(serializeResponse(response));
            buffer.flip();
            channel.write(buffer);
            logger.debug("Wrote {} bytes to client", buffer.limit());
//            for (var i : serializeResponse(response)) {
//                System.out.print(i + " ");
//            }
//            System.out.println();


            buffer.clear();
            channel.close();
        } catch (SocketException e) {
            logger.error("Socket exception!", e);
        } catch (IOException e) {
            logger.error("Error in Write", e);
        }

    }


}
