/***
 * ServerTCP
 * Date: 10/01/04
 * Authors:
 */

package stream;

import java.io.*;
import java.net.*;
import java.util.*;

public class ServerTCP {
  
  
  public static ArrayList<ServerConnectionThread> connections = new ArrayList<ServerConnectionThread>();
          
  public static void main(String args[]){ 
    
    ServerSocket listenSocket;

  	if (args.length != 1) {
          System.out.println("Usage: java ServerTCP <ServerTCP port>");
          System.exit(1);
  	}

    try {

      listenSocket = new ServerSocket(Integer.parseInt(args[0])); //port
      System.out.println("Server ready..."); 

      while (true) {
        Socket socket = listenSocket.accept();
        System.out.println("Connexion from:" + socket.getInetAddress());
        ServerConnectionThread sct = new ServerConnectionThread(socket);
        sct.start();
        connections.add(sct);
      }
    } catch (Exception e) {
        System.err.println("Error in ServerTCP:" + e);
        e.printStackTrace();
    }
  }	
}

  
