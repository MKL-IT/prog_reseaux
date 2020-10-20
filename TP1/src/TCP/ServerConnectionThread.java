
package TCP;

import java.io.*;
import java.net.*;
import history.Message;


public class ServerConnectionThread extends Thread {
	
	private Socket socket;
  private String pseudo;

  BufferedReader socIn;
  PrintStream socOut;
	
	ServerConnectionThread(Socket s) {
		this.socket = s;
	}


/*
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

*/

  public void sendMessage(String message) {
    try {
      socOut.println(message);
    } catch (Exception e) {
          System.err.println("Error in EchoServer:" + e); 
        }
  }

  
  public String getClientAddress () {
    return socket.getInetAddress().toString();
  }

	public void run() {

    try {

      socIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));    
      socOut = new PrintStream(socket.getOutputStream());

      socOut.println("To join enter : join <pseudo>");

      while (true) {
        String line = socIn.readLine();
        
        if (line != null){
          if(line.startsWith("join")) {

            String [] params = line.split(" ");
          
            if (params.length == 2) {
            
              if (ServerTCP.join(this, params[1])) {
                pseudo = params[1];
              }
            } else {
              socOut.println("To join enter : join <pseudo>");
            }

          } else if( (line.equals("leave") || line.equals(".") )&& pseudo != null) {
            ServerTCP.leave(pseudo);

          } else if (pseudo != null) {
          
              Message msg = new Message(pseudo, line);
              ServerTCP.diffuseMessage(msg);
            
          } else {
            socOut.println("You are not registered");
          }
        }

      }

    } catch (Exception e) {
      System.err.println("Error in ServerConnectionThread:" + e); 
      e.printStackTrace();
    }
  }
  
}
