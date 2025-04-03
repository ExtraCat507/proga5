package org.xtracat.connection.util;
import java.nio.ByteBuffer;

public class ClientData {
    public ByteBuffer buffer;
    public ClientData() {
        this.buffer = ByteBuffer.allocateDirect(1024);
    }
}
