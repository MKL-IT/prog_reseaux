/***
 * Message
 * Date: 20/10/2020
 * Authors: ESSAYED Sana, MATOKA Lea / B3-10
 */

package history;

public class Message {

	protected String user;
	protected String message;
	
	/**
  	* constructor Message
	* @param user
	* @param message 
  	**/
	public Message(String user, String message) {
		this.user = user;
		this.message = message;
	}
	
	/**
  	* methode Message
  	* pour lire dans le fichier persistant
	* @param line
  	**/
	public Message(String line) {
		String[] array = line.split("-#", 2);
		this.user = array[0];
		this.message = array[1];
	}

	/**
  	* getter getUser
	* attribute user
  	**/
	public String getUser () {
		return user;
	}

	/**
	* getter getMessageOnly
	* attribute message
	**/
	public String getMessageOnly () {
		return message;
	}
	
	/**
	* method getMessage
	* affichage d'un message côté client 
	**/
	public String getMessage () {

		int size = user.length();
		size = 12 - size;

		String spaces = "";
		for(int i = 0; i<size; i++)
			spaces += " ";

		return (" - "+user + spaces +" -> " + message);
	}
	
	/**
	* method toString
	* pour écrire dans le fichier persistant
	**/
	public String toString () {
		return (user+"-#"+message);
	}	
}
