/***
 * WebServer
 * Mini WebServer
 * Date: 20/10/2020
 * @author: B3-10 / ESSAYED Sana, MATOKA Lea
 ***/

package http.server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class WebServer {

    /**
    * methode main
    * @param port
    **/
    public static void main(String args[]) {
        ServerSocket s = null;

        if (args.length != 1) {
            System.out.println("Usage: java WebServer <Server port>");
            System.exit(1);
        }

        System.out.println("Webserver starting up on port " + args[0]);
        System.out.println("(press ctrl-c to exit)");

        try {
            // create the main server socket
            s = new ServerSocket(Integer.parseInt(args[0]));
        } catch (Exception e) {
            System.out.println("Error: " + e);
            System.exit(1);
        }

        System.out.println("Waiting for connection");

        try {
            while (true) {
                Socket socket  = s.accept();
                
                ServerThread st = new ServerThread(socket);
                st.start();
                // remote is now the connected socket
                System.out.println("New connection, sending data.");

            }

        } catch (Exception e) {
            System.out.println("Error in WebServer: " + e);
            
            e.printStackTrace();
        }

    }

}
