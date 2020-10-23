/***
* MulticastClient
* UDP Chat
* Date: 20/10/2020
* @author: B3-10 / ESSAYED Sana, MATOKA Lea
* 
*/


package UDP;

import java.io.*;
import java.net.*;


public class MulticastClient  {

  /**
  * methode main
  * @param host
  * @param port
  * 
  * Gestion de nouvelle connexion
  * Ecrire d'un message et envoi aux autres membres du groupe
  *
  **/
	public static void main(String[] args) throws IOException {


      BufferedReader stdIn = null;
      MulticastSocket multiSocket = null;
      InetAddress groupAddr = null;  
      Integer groupPort = null;

      MulticastClientThread mct = null;

      if (args.length != 2) {
        System.out.println("Usage: java MulticastClient <Multicast host> <Multicast port>");
        System.exit(1);
      }

      try {

        // Group IP address
    		groupAddr = InetAddress.getByName(args[0]);   // 228.5.6.7
    		groupPort =  Integer.parseInt(args[1]); // 6789

          	// Create a multicast socket
    		multiSocket = new MulticastSocket(groupPort);

      	   	// Join the group
    		multiSocket.joinGroup(groupAddr);

    		mct = new MulticastClientThread(multiSocket);
    		mct.start();
      	
        stdIn = new BufferedReader(new InputStreamReader(System.in));

        System.out.println();
        System.out.println("Welcome to the multicast Server");

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
                           
      String msg;

      while (true) {
      	msg=stdIn.readLine();

      	if (msg.equals(".")){
      		break;
      	}

      // Build a datagram packet for a message
  		// to send to the group
  		DatagramPacket packet = new
  		DatagramPacket(msg.getBytes(),
  		msg.length(), groupAddr, groupPort);
  		// Send a multicast message to the group
  		multiSocket.send(packet); 
      }
    
    stdIn.close();

    // OK, I'm done talking - leave the group
  	multiSocket.leaveGroup(groupAddr); 
  	multiSocket.close();
  }
}