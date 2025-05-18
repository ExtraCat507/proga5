package org.xtracat;

import org.slf4j.Logger;
import org.xtracat.singleton.SingletonLogger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.channels.Pipe;

public class ServerConsole {
    private final Logger logger = SingletonLogger.getLogger();
    private Pipe.SinkChannel sinkChannel;


    public ServerConsole(Pipe.SinkChannel sinkChannel){
        this.sinkChannel = sinkChannel;
    }

    public void run(){
        Pipe.SinkChannel consoleSink = this.sinkChannel;
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try {
            String line;
            while ((line = reader.readLine()) != null) {
                byte[] bytes = (line + "\n").getBytes();
                ByteBuffer buf = ByteBuffer.wrap(bytes);
                while (buf.hasRemaining()) {
                    consoleSink.write(buf);
                }
            }
        } catch (IOException e) {
            logger.error("Error reading from System.in", e);
        }
    }

}
