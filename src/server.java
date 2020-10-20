import java.net.*;
import java.io.*;

public class server{
    public static void main(String[]  args){
        try{
            ServerSocket ss = new ServerSocket(3000);
            Socket new_s = ss.accept();
        }
        catch(IOException err){
            System.out.println("Error on server");    
        }
        System.out.println("Connection sUccess");
    }
}
