package router;

import java.net.Socket;
import java.net.ServerSocket;
import java.io.IOException;

public class Router {
    public static void main(String[] args){

        ServerSocket brokerServer = null;
        Socket brokerSoc = null;
        try{
            brokerServer = new ServerSocket(5000);
            System.out.println("Waiting...");
            brokerSoc = brokerServer.accept();
            System.out.println("Connected with broker");
            
            brokerServer.close();
            brokerSoc.close();
        }
        catch(IOException errRouter){
            System.out.println("Error on Router");
            errRouter.printStackTrace();
        }
        System.out.println("Code Continues");
    }    
}
