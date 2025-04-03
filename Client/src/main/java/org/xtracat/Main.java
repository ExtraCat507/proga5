package org.xtracat;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;


public class Main {
    public static void main(String[] args) {

        System.out.println("Clientside running");
        try {

            byte arr[] = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
            int len = arr.length;

            Socket sock;
            OutputStream os;
            InputStream is;
            InetAddress host;
            int port;
            port = 6789;
            host = InetAddress.getLocalHost(); //пока чо
            sock = new Socket(host, port);


            os = sock.getOutputStream();
            os.write(arr);
            is = sock.getInputStream();
            is.read(arr);

            for (byte j : arr) {
                System.out.println(j);
            }


        } catch (UnknownHostException e) {
            System.out.println("где локалхост вася");
        } catch (IOException e) {
            System.out.println("Вася все хуйня");
            e.printStackTrace();
        }

    }
}