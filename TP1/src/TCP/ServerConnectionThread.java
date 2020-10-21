/***
 * ServerConnectionThread
 * Exemple serveur TCP
 * Date: 20/10/2020
 * @author: B3-10 / ESSAYED Sana, MATOKA Lea
 */

package TCP;

import java.io.*;
import java.net.*;
import history.Message;


public class ServerConnectionThread extends Thread {
	
	private Socket socket;
  private String pseudo;

  BufferedReader socIn;
  PrintStream socOut;
  boolean joined;
	
  /**
  * constructeur ServerConnectionThread
  * @param s Socket
  **/
	ServerConnectionThread(Socket s) {
		this.socket = s;
    this.joined = false;
	}


  /**
  * methode sendMessage
  * @param message
  **/
  public void sendMessage(String message) {
    try {
      socOut.println(message);
    } catch (Exception e) {
          System.err.println("Error in EchoServer:" + e); 
        }
  }

  /**
  * methode getClientAddress
  **/
  public String getClientAddress () {
    return socket.getInetAddress().toString();
  }

  public boolean isJoined(){
    return joined;
  }

  public void joinChat(){
    joined = true;
  }

  public void leaveChat(){
    joined = false;
  }

  /**
  * methode run
  * @param pseudo
  **/
	public void run() {

    try {

      socIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));    
      socOut = new PrintStream(socket.getOutputStream());

      socOut.println("To join enter : join <pseudo>");

      while (true) {
        String line = socIn.readLine();
        
        if (line != null){

          if(line.startsWith("join") && !isJoined()) {

            String [] params = line.split(" ");
          
            if (params.length == 2) {
            
              if (ServerTCP.join(this, params[1])) {
                pseudo = params[1];
              }
            } else {
              socOut.println("To join enter : join <pseudo>");
            }

          } else if( (line.equals("leave") )&& pseudo != null) {
            ServerTCP.leave(pseudo);
            leaveChat();

          }else if (line.equals(".")){
            ServerTCP.leave(pseudo);
            break;
            
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
