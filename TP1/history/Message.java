package history;

public class Message {

	protected String user;
	protected String message;
	

	// constructor Message
	public Message(String user, String message) {
		this.user = user;
		this.message = message;
	}
	
	// method
	// pour lire dans le fichier persistent
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
	
	// method
	// affichage d'un message côté client 
	public String getMessage () {

		int size = user.length();
		size = 12 - size;

		String spaces = "";
		for(int i = 0; i<size; i++)
			spaces += " ";

		return (" - "+user + spaces +" -> " + message);
	}
	
	// method toString
	// pour écrire dans le fichier persistant
	public String toString () {
		return (user+"-#"+message);
	}	
}
