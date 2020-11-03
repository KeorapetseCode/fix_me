package router;

import java.net.Socket;
import java.net.ServerSocket;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Router {
    public static void main(String[] args){

        ServerSocket server = null;
        Socket soc = null;
        try{
            server = new ServerSocket(5000);
            System.out.println("Waiting...");
            soc = server.accept();

            System.out.println("Connected with broker");
           
            
            InputStreamReader inReader = new InputStreamReader(soc.getInputStream());
            BufferedReader bufRead = new BufferedReader(inReader);

            String frmBroker = bufRead.readLine();
            System.out.println("From Broker " + frmBroker + "\n");

            server.close();
            soc.close();
        }
        catch(IOException errRouter){
            System.out.println("Error on Router");
            errRouter.printStackTrace();
        }
        System.out.println("Code Continues");
    }    
}
