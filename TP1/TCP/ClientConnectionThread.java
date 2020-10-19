
package stream;

import java.io.*;
import java.net.*;

public class ClientConnectionThread extends Thread {
	
	private Socket socket;
  BufferedReader socIn;
  PrintStream socOut = null;
  boolean go_on ;

	
	ClientConnectionThread(Socket s) {
    go_on = true;
		this.socket = s;
	}

  public void stopThread(){
    go_on = false;
  }

  public void sendToServer(String text){

    try {

      socOut.println(text);
        
    } catch (Exception e) {
        System.err.println("Error in sendToServer:" + e); 
        e.printStackTrace();

    }
  }


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
