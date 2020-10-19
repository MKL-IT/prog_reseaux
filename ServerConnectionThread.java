
package stream;

import java.io.*;
import java.net.*;

public class ServerConnectionThread extends Thread {
	
	private Socket socket;
  BufferedReader socIn;
  PrintStream socOut;
	
	ServerConnectionThread(Socket s) {
		this.socket = s;
	}


  public void deleteClient(){

  }

  public synchronized void sendToClient(String text, int numClient){

    try {
      socOut.println(numClient + " : " +text);
      
    } catch (Exception e) {
        System.err.println("Error in sendToClient:" + e); 
        e.printStackTrace();
    }
    
  }

  public void sendToAllClients(String text, int numClient){


    for(int i = 0; i < ServerTCP.connections.size(); i++){
      ServerConnectionThread sct = ServerTCP.connections.get(i);

      if ( sct.socket.isConnected() ){
        sct.sendToClient(text, numClient);
      }
      
    }
  }

	public void run() {

    try {

      socIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));    
      socOut = new PrintStream(socket.getOutputStream());

      while (true) {
        
        String line = socIn.readLine();

        if(line != null)
          sendToAllClients(line, socket.getPort());
      }
    } catch (Exception e) {
      System.err.println("Error in ServerConnectionThread:" + e); 
      e.printStackTrace();
    }
  }
  
}
