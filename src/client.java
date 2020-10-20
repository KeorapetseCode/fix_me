import java.net.*;
import java.io.*;

public class client{
    public static void main(String[]  args){
        try{
            Socket s = new Socket("localhost", 3000);
            s.close();
        }
        catch (IOException err){
            System.out.println("Other type of message");
        }
    }
}
