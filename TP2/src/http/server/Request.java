/***
 * Request
 * Date: 20/10/2020
 * @author: B3-10 / ESSAYED Sana, MATOKA Lea
 ***/

package http.server;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.util.ArrayList;

public class Request {

    private BufferedInputStream in;

    private String method;
    private String uri;
    private String httpVersion;
    private ArrayList<String> fields;
    private String body;

    /**
    * constructeur d'une requete
    * @param input
    **/
    public Request(BufferedInputStream input) {
        this.in = input;
    }

    /**
    * methode readRequest
    **/
    public void readRequest() {
        // Lire la requete et remplir les attributs
        // Le header se termine par la séquence \r\n\r\n (CR LF CR LF)
        int bcur = '\0';
        int bprec = '\0';
        boolean newline = false;

        String header = new String();
        try {
            while ((bcur = in.read()) != -1 && !(newline && bprec == '\r' && bcur == '\n')) {
                if (bprec == '\r' && bcur == '\n') {
                    newline = true;
                } else if (!(bprec == '\n' && bcur == '\r')) {
                    newline = false;
                }
                bprec = bcur;
                header += (char) bcur;
            }
        } catch (Exception e) {
            System.err.println("Error in readRequest: " + e);
            e.printStackTrace();
        }

        int wordEnd = header.indexOf(" ");
        if (wordEnd != -1) {
            method = header.substring(0, wordEnd);
            header = header.substring((wordEnd + 1));
        }
        wordEnd = header.indexOf(" ");
        if (wordEnd != -1) {
            uri = header.substring(0, wordEnd);
            //uri = "../" + request_uri;
            uri = uri;
            header = header.substring((wordEnd + 1));
        }
        wordEnd = header.indexOf("\r\n");
        if (wordEnd != -1) {
            httpVersion = header.substring(0, wordEnd);
            header = header.substring((wordEnd + 2));
        }
        
        //Lecture fields
        fields = new ArrayList<String>();
        while (header.length() > 4) {
            wordEnd = header.indexOf("\r\n");
            if (wordEnd != -1) {
                String field = header.substring(0, wordEnd);
                fields.add(field);
                header = header.substring((wordEnd + 2));
            }
        }
         
        // Body de la requête....

    }

    /**
    * getter getMethod
    **/
    public String getMethod() {
        return method;
    }

    /**
    * getter getUri
    **/
    public String getUri() {
        return uri;
    }

    /**
    * getter getHttpVersion
    **/
    public String getHttpVersion() {
        return httpVersion;
    }

    /**
    * getter getFields
    **/
    public ArrayList<String> getFields() {
        return fields;
    }

    /**
    * getter getBody
    **/
    public String getBody() {
        return body;
    }
    
    

}
