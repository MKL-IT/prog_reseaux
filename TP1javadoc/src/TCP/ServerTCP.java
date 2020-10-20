/***
 * ServerTCP
 * Exemple serveur TCP
 * Date: 20/10/2020
 * Authors: ESSAYED Sana, MATOKA Lea / B3-10
 */

package TCP;

import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import history.*;

public class ServerTCP  {
	

    private static HashMap<String, ServerConnectionThread> connections = new HashMap<String, ServerConnectionThread>();

	private static List<Message> msg_history = new ArrayList<Message>();
	protected static PersistHistory persist = new PersistHistory("history");
  
 	/**
  	* main method ServerTCP 
	* @param port 
  	**/
    public static void main(String args[]){ 

        ServerSocket listenSocket;
        
	  	if (args.length != 1) {
	        System.out.println("Usage: java ServerTCP <ServerTCP port>");
	        System.exit(1);
	  	}

        persist.createHistoryFile();
        loadMessagesFromHistoryFile();

		try {
			listenSocket = new ServerSocket(Integer.parseInt(args[0])); //port

			System.out.println("Server ready...");

			while (true) {
				Socket socket = listenSocket.accept();
                System.out.println("Connexion from:" + socket.getInetAddress());
                ServerConnectionThread sct = new ServerConnectionThread(socket);
                sct.start();
			}

        } catch (Exception e) {
            System.err.println("Error in ServerTCP:" + e);
            e.printStackTrace();
        }
    }


    /**
    * methode join
    * @param sct ServerConnectionThread
    * @param pseudo 
    **/   
    public static synchronized boolean join(ServerConnectionThread sct, String pseudo) {
    	
        if(connections.containsKey(pseudo)){
    		
            //System.out.println("User " + pseudo + " already exist");
    		sct.sendMessage("User " + pseudo + " already exists");
    		return false;

    	} else {
    		//System.out.println(pseudo + " joined the server with adress : " + sct.getClientAddress());
        	
            connections.put(pseudo, sct);

        	Message msg = new Message(pseudo,pseudo + " joined the server");
        	welcomeUser(pseudo);
        	diffuseTempMessage(msg);

        	return true;
    	}
    }
    
    /**
    * methode leave
    * @param pseudo 
    **/ 
    public static synchronized void leave(String pseudo) {
    	System.out.println("A user has left the server : " + connections.get(pseudo).getClientAddress() ); 
    	Message msg = new Message(pseudo,pseudo + " left the server");
    	diffuseTempMessage(msg);
    	connections.remove(pseudo);
    }


    /**
    * methode diffuseMessage
    * @param message 
    **/
    public static synchronized void diffuseMessage(Message message) {

    	if(connections.containsKey(message.getUser())){
    		msg_history.add(message);
    		writeMessageToHistoryFile(message);
    		sendMessageClients(message.getMessage());
    	}
    }
    
    /**
    * method diffuseTempMessage
    * @param message 
    **/
    public static synchronized void diffuseTempMessage(Message message){
    	if(connections.containsKey(message.getUser())){
    		sendMessageClients(message.getMessageOnly());
    	}
    }
    
    /**
    * method welcomeUser
    * @param user 
    **/
    public static void welcomeUser(String user) {
    	ServerConnectionThread sct = connections.get(user);
    	if (sct!=null) {
    		sct.sendMessage("Welcome to the server ! You are now talking with " + (connections.size() - 1) + " people");
        	for(Message m : msg_history){
            	sct.sendMessage(m.getMessage());
            }
    	}
    }
    
    //public static synchronized void diffuseMessage(String message) {
    	//System.out.println("New message : " + message); 
    	//sendMessageClients(message);
    //}
    
    /**
    * methode sendMessageClients
    * @param message 
    **/
    public static void sendMessageClients(String message) {
    	for(ServerConnectionThread sct : connections.values()){
        	sct.sendMessage(message);
        }
    }
    
    /**
    * methode writeMessageToHistoryFile
    * @param message 
    **/
    public static void writeMessageToHistoryFile (Message message) {
    	persist.appendToHistoryFile(message.toString());
    }
    
    /**
    * methode loadMessagesFromHistoryFile
    **/
    public static void loadMessagesFromHistoryFile () {
    	List<String> lines = persist.readFromHistoryFile();
    	for(String line : lines) {
    		Message message = new Message (line);
    		msg_history.add(message);
    	}
    }
    
}

  
