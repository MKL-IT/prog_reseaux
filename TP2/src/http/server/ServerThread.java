/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package http.server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.ArrayList;

/**
 *
 * @author lea
 */
public class ServerThread extends Thread {

    private Socket socket;

    public ServerThread(Socket s) {
        this.socket = s;
    }

    public void run() {


        try {
            BufferedInputStream in = new BufferedInputStream( socket.getInputStream() );
            BufferedOutputStream out = new BufferedOutputStream(socket.getOutputStream());

            
            while(true) {

                //Gérer la requête
                Request request = null;
                request = new Request(in);
                request.readRequest();

                

                if(request != null){

                    System.out.println(request.getMethod());
                    System.out.println(request.getUri());

                    switch (request.getMethod()) {

                        case "GET":
                            methodGET(out, request.getUri());
                            break;

                        case "HEAD":
                            methodHEAD(out, request.getUri());
                            break;

                        case "POST":
                            methodPOST(out, in, request.getUri());
                            break;

                        case "PUT":
                            methodPUT(out, in, request.getUri());
                            break;
                    
                        case "DELETE":
                            methodDELETE(out, request.getUri(), request.getFields() );
                            break;

                        default:
                            break;
                    }
                
                }
             
            
            }

            //Fermer la socket
        } catch (Exception e) {
            System.err.println("Error in ServerThread: " + e);
            e.printStackTrace();
        }
    }

    private void methodGET(BufferedOutputStream out, String request_uri) {
        //Répond à une requete GET
        File file = new File(request_uri);
        BufferedReader reader = null;

        String code = null;
        String toSend = new String();
        
        
        
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            int cur = '\0';

            while ((cur = reader.read()) != -1) {
            }
            code = "200 OK";
            
        } catch (FileNotFoundException ex) {
            System.err.println("Error in httpGET: " + ex);
            ex.printStackTrace();
            
            if(file.isFile()) {
                code = "403 Forbidden";
            } else {
                code = "404 Not Found";
            }

        } catch (IOException ex) {
            System.err.println("Error in httpGET: " + ex);
            code = "404 Not Found";
            ex.printStackTrace();
        }

        String type = "...";
        long length = 0;

        switch (code) {

            case "200 OK":
                type = getContentType(file);
                length = file.length();

                break;

            case "404 Not Found":
                type = "Content-Type: text/html\r\n";
                length = (long) NOTFOUND.length();
                toSend = NOTFOUND;
                break;
            
            case "403 Forbidden":
                type = "Content-Type: text/html\r\n";
                length = (long) FORBIDDEN.length();
                toSend = FORBIDDEN;
                break;
                
            
            default:
                break;
        }

        try {
            out.write(makeHeader(code, type, length).getBytes());

            if ( code == "200 OK"){

                // Ouverture d'un flux de lecture binaire sur le fichier demand�
                BufferedInputStream fileIn = new BufferedInputStream(new FileInputStream(file));
                // Envoi du corps : le fichier (page HTML, image, vidéo...)
                byte[] buffer = new byte[256];
                int nbRead;
                while((nbRead = fileIn.read(buffer)) != -1) {
                    out.write(buffer, 0, nbRead);
                }
                // Fermeture du flux de lecture
                fileIn.close();

            }else{
                out.write(toSend.getBytes());
            }
            
            //envoi de la réponse
            out.flush();
        } catch (IOException ex) {
            System.err.println("Error in httpGET: " + ex);
            ex.printStackTrace();
        }

    }



    private void methodHEAD(BufferedOutputStream out, String request_uri) {
        

        //Réponse à une requête HEAD

        File file = new File(request_uri);
        BufferedReader reader = null;
        String code = null;
               
        
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            
            int cur = ' ';

            while ((cur = reader.read()) != -1) {
            }
            code = "200 OK";
            
        } catch (FileNotFoundException ex) {
            System.err.println("Error in methodGET: " + ex);
            ex.printStackTrace();
            
            if(file.isFile()) {
                code = "403 Forbidden";
            } else {
                code = "404 Not Found";
            }

        } catch (IOException ex) {
            System.err.println("Error in methodGET: " + ex);
            code = "404 Not Found";
            ex.printStackTrace();
        }

        String type = "";
        long length = 0;

        switch (code) {

            case "200 OK":
                type = getContentType(file);
                length = file.length();

                break;

            case "404 Not Found":
                type = "Content-Type: text/html\r\n";
                length = (long) NOTFOUND.length();
                break;
            
            case "403 Forbidden":
                type = "Content-Type: text/html\r\n";
                length = (long) FORBIDDEN.length();
                break;
                
            
            default:
                break;
        }

        try {
            out.write(makeHeader(code, type, length).getBytes());
            out.flush();
        } catch (IOException ex) {
            System.err.println("Error in httpGET: " + ex);
            ex.printStackTrace();
        }

    }


     private void methodPOST(BufferedOutputStream out, BufferedInputStream in, String request_uri) {
        
        try {

            File src = new File(request_uri);
            boolean create = src.createNewFile();
            

            BufferedOutputStream bufOut = new BufferedOutputStream(new FileOutputStream(src, src.exists()));

            byte[] buffer = new byte[256];
            while(in.available() > 0) {
                int nbRead = in.read(buffer);
                bufOut.write(buffer, 0, nbRead);
            }
            bufOut.flush();
            bufOut.close();
            
            if (create) {
                out.write(makeHeader("201 Created").getBytes());
                out.write("\r\n".getBytes());
            } else {
                out.write(makeHeader("200 OK").getBytes());
                out.write("\r\n".getBytes());
            }
            
            // envoi de la réponse
            out.flush();

        } catch (Exception e) {

            e.printStackTrace();
            try {
                out.write(makeHeader("500 Internal Server Error").getBytes());
                //out.write("\r\n".getBytes());
                out.flush();

            } catch (Exception e2) {
                System.out.println(e);
                e2.printStackTrace();
            }
        }
    }


    private void methodPUT(BufferedOutputStream out, BufferedInputStream in, String request_uri) {

        try {
            File src = new File(request_uri);
            if (!src.createNewFile()){
                PrintWriter pw = new PrintWriter(src);
                pw.close();
            }
            
            BufferedOutputStream bufOut = new BufferedOutputStream(new FileOutputStream(src, src.exists()));

            byte[] buffer = new byte[256];
            while(in.available() > 0) {
                int nbRead = in.read(buffer);
                bufOut.write(buffer, 0, nbRead);
            }

            bufOut.flush();
            bufOut.close();

            if (src.createNewFile()) {
                out.write(makeHeader("204 No Content").getBytes());
                out.write("\r\n".getBytes());
            } else {
                out.write(makeHeader("201 Created").getBytes());
                out.write("\r\n".getBytes());
            }

            //envoi de la réponse
            out.flush();

        } catch (Exception e) {
            e.printStackTrace();
            try {
                out.write(makeHeader("500 Internal Server Error").getBytes());
                out.write("\r\n".getBytes());
                out.flush();
            } catch (Exception e2) {
                System.out.println(e);
            }

        }
    }
    
    private void methodDELETE(BufferedOutputStream out, String request_uri, ArrayList<String> fields) {
        
        //Répond à une requete DELETE
        File file = new File(request_uri);
        BufferedReader reader = null;
       
       
        String code = null;
        String toSend = new String();
 
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            int cur = '\0';
        
            
            while ((cur = reader.read()) != -1 ) {
            }

            file.delete();

            if( fields.size() > 0){
                // action a été confirmée et que le message de réponse
                // inclut une représentation décrivant le statut
                code = "200 OK";
            }else{
                // action a été confirmée et qu'aucune information supplémentaire n'est à fournir
                code = "204 No Content";
            }
                  
            
        } catch (FileNotFoundException ex) {
            System.err.println("Error in httpGET: " + ex);
            ex.printStackTrace();
            
            if(file.isFile()) {
                // client n'a pas les droits d'accès au contenu
                code = "403 Forbidden";
            } else {
                // serveur n'a pas trouvé la ressource demandée
                code = "404 Not Found";
            }

        } catch (IOException ex) {
            System.err.println("Error in httpGET: " + ex);
            code = "404 Not Found";
            ex.printStackTrace();
        }

        String type = "";
        long length = 0;

        switch (code) {

            
            case "200 OK":
                type = "Content-Type: text/html\r\n";
                length = (long) DELETED.length();
                toSend = DELETED;
                break;

            case "204 No Content":
                type = "Content-Type: text/html\r\n";
                length = (long) DELETED.length();
                toSend = DELETED;
                break;

            case "404 Not Found":
                type = "Content-Type: text/html\r\n";
                length = (long) NOTFOUND.length();
                toSend = NOTFOUND;
                break;
            
            case "403 Forbidden":
                type = "Content-Type: text/html\r\n";
                length = (long) FORBIDDEN.length();
                toSend = FORBIDDEN;
                break;
                
            
            default:
                break;
        }

        try {

            if ( code == "204 No Content"){
                out.write(makeHeader(code).getBytes());
            }else{
                out.write(makeHeader(code, type, length).getBytes());
                out.write(toSend.getBytes());
            }
            
            out.flush();

        } catch (IOException ex) {
            System.err.println("Error in httpGET: " + ex);
            ex.printStackTrace();
        }

    }
    


    public String getContentType(File file) {

        String fileName = file.getName();
        String type = ".txt";

        if (fileName.endsWith(".html") || fileName.endsWith(".htm") || fileName.endsWith(".txt")) {
            type = "Content-Type: text/html\r\n";
        } else if (fileName.endsWith(".mp4")) {
            type = "Content-Type: video/mp4\r\n";
        } else if (fileName.endsWith(".png")) {
            type = "Content-Type: image/png\r\n";
        } else if (fileName.endsWith(".jpeg") || fileName.endsWith(".jpg")) {
            type = "Content-Type: image/jpeg\r\n";
        } else if (fileName.endsWith(".mp3")) {
            type = "Content-Type: audio/mp3\r\n";
        } else if (fileName.endsWith(".avi")) {
            type = "Content-Type: video/x-msvideo\r\n";
        } else if (fileName.endsWith(".css")) {
            type = "Content-Type: text/css\r\n";
        } else if (fileName.endsWith(".pdf")) {
            type = "Content-Type: application/pdf\r\n";
        } else if (fileName.endsWith(".odt")) {
            type = "Content-Type: application/vnd.oasis.opendocument.text\r\n";
        } else if (fileName.endsWith(".json")) {
            type = "Content-Type: application/json\r\n";
        }

        return type;
    }


    private String makeHeader(String code, String type, long length) {

        String header;

        header = "HTTP/1.1 " + code + "\r\n";
        header += type;
        header += "Content-Length: " + length + "\r\n";
        header += "Server: Mini WebServer\r\n";
        header += "\r\n";
        return header;
    }

    private String makeHeader(String code) {
        String header = "HTTP/1.1 " + code + "\r\n";
        header += "Server: Mini WebServer\r\n";
        header += "\r\n";
        return header;
    }
    
    

    private static final String DELETED =
            "<!DOCTYPE HTML PUBLIC \"-//IETF//DTD HTML 2.0//EN\">\n"
            +"<html>\n"
            +"<body>\n"
            +"   <h1>File deleted.</h1>\n"
            +"</body>\n"
            +"</html>";

    private static final String NOTFOUND = "<!DOCTYPE HTML PUBLIC \"-//IETF//DTD HTML 2.0//EN\">\n"
            + "<html>\n"
            + "<head>\n"
            + "   <title>404 Not Found</title>\n"
            + "</head>\n"
            + "<body>\n"
            + "   <h1>404 Not Found</h1>\n"
            + "   <p>The requested URL was not found on this server.</p>\n"
            + "</body>\n"
            + "</html> ";
    
    private static final String FORBIDDEN = "<!DOCTYPE HTML PUBLIC \"-//IETF//DTD HTML 2.0//EN\">\n"
            + "<html>\n"
            + "<head>\n"
            + "   <title>403 Forbidden</title>\n"
            + "</head>\n"
            + "<body>\n"
            + "   <h1>403 Forbidden</h1>\n"
            + "   <p>Access is forbidden to the requested page.</p>\n"
            + "</body>\n"
            + "</html> ";
    
}
