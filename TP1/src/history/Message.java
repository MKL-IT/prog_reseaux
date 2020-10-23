/***
 * Message
 * Date: 23/10/2020
 * @author: B3-10 / ESSAYED Sana, MATOKA Lea
 ***/

package history;

public class Message {

	protected String user;
	protected String message;
	
	/**
  	* constructor Message 
	* @param user
    * @param message
    * 
  	**/
	public Message(String user, String message) {
		this.user = user;
		this.message = message;
	}
	
	/**
  	* constructor Message 
	* @param line depuis fichier persistant
  	**/
	public Message(String line) {
		String[] array = line.split("-#", 2);
		this.user = array[0];
		this.message = array[1];
	}


	// getter
	// attribute user
	public String getUser () {
		return user;
	}

	// getter
	// attribute message
	public String getMessageOnly () {
		return message;
	}
	

	/**
  	* constructor getMessage 
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
  	* methode toString
	* pour écriture dans le fichier persistant
  	**/
	public String toString () {
		return (user+"-#"+message);
	}	
}
