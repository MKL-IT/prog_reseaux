/***
 * ClientConnexionThread
 * Exemple serveur TCP
 * Date: 20/10/2020
 * Authors: ESSAYED Sana, MATOKA Lea / B3-10
 */


package TCP;

import java.io.*;
import java.net.*;

public class ClientConnectionThread extends Thread {
	
	private Socket socket;
  BufferedReader socIn;
  PrintStream socOut = null;

	/**
  * constructeur ClientConnectionThread
  * @param s
  **/
	ClientConnectionThread(Socket s) {
		this.socket = s;
	}

  /**
  * methode sendToServer
  * @param text
  **/
  public void sendToServer(String text){

    try {

      socOut.println(text);
        
    } catch (Exception e) {
        System.err.println("Error in sendToServer:" + e); 
        e.printStackTrace();

    }
  }

  /**
  * methode run
  **/
	public void run() {

    try {

        BufferedReader socIn = null;
        socIn = new BufferedReader(
          new InputStreamReader(socket.getInputStream()));    
        socOut= new PrintStream(socket.getOutputStream());

        while (true) {
          String line = socIn.readLine();
          System.out.println(line); 
          
        }
          
    } catch (SocketException se){
      System.exit(1);

    }catch (Exception e) {
        System.err.println("Error in ClientConnectionThread:" + e); 
        e.printStackTrace();

    }
    
  }

}
