/***
 * EchoClient
 * Example of a TCP client 
 * Date: 10/01/04
 * Authors:
 */
package TCP;

import java.io.*;
import java.net.*;


public class ClientTCP {

  /**
  *  main method
  *  accepts a connection, receives a message from client then sends an echo to the client
  **/
    public static void main(String[] args) throws IOException {
      
      ClientConnectionThread cct = null;
      Socket socket = null;
      BufferedReader stdIn = null;


      if (args.length != 2) {
        System.out.println("Usage: java ClientTCP <EchoServer host> <EchoServer port>");
        System.exit(1);
      }

      try {
  	    // creation socket ==> connexion
  	    socket = new Socket(args[0], new Integer(args[1]).intValue());
        cct = new ClientConnectionThread(socket);
        cct.start();

        stdIn = new BufferedReader(new InputStreamReader(System.in));

      } catch (UnknownHostException e) {
          System.err.println("Don't know about host:" + args[0]);
          e.printStackTrace();
          System.exit(1);
      } catch (IOException e) {
          System.err.println("Couldn't get I/O for "
                             + "the connection to:"+ args[0]);
          e.printStackTrace();
          System.exit(1);
      }

      String line;
      while (true) {
      	
        line=stdIn.readLine();

        cct.sendToServer(line);

      	if (line.equals(".") )
          break;
      }

      stdIn.close();
      socket.close();
  }
}


