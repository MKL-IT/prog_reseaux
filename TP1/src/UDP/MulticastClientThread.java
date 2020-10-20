/***
 * MulticastClientThread
 * Exemple serveur UDP
 * Date: 20/10/2020
 * @author: B3-10 / ESSAYED Sana, MATOKA Lea
 */

package stream;

import java.io.*;
import java.net.*;

public class MulticastClientThread extends Thread {
	
	private MulticastSocket multiSocket;
  boolean go_on;
	
  /**
  * constructeur MulticastClientThread
  * @param ms MulticasSocket
  **/
	MulticastClientThread (MulticastSocket ms) {
		this.multiSocket = ms;
    go_on = true;
	}

  /*public void interrupt() {
      super.interrupt();
      try {
        multiSocket.close(); // Fermeture du flux si l'interruption n'a pas fonctionné.
      } catch (SocketException se) {}
  }*/

 	/**
  * receives a request from client then sends an echo to the client
  * @param clientSocket the client socket
  **/
	public void run() {
  	  try {
    		
        while (true) {

    		  // Build a datagram packet for response
          byte[] buf = new byte[1000];
          DatagramPacket recv = new
          DatagramPacket(buf, buf.length);

          // Receive a datagram packet response
          multiSocket.receive(recv);

          // Print the response
          System.out.println(new String(buf));
          
  		  }

      } catch (SocketException se){
          System.exit(1);

    	} catch (Exception e) {
        	System.err.println("Error in EchoServer:" + e); 
      }
  }
  
}

  