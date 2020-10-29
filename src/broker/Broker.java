package broker;

import java.net.Socket;
import java.io.IOException;

public class Broker {
    public static void main(String[] args){
        try{
            Socket brokerSocket = new Socket("localhost", 5000);
            brokerSocket.close();
        }
        catch(IOException err){
            err.printStackTrace();
        }
        
    }
}
