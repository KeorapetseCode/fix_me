package broker;

import java.net.Socket;
import java.io.IOException;
import java.util.Scanner;
import java.io.PrintWriter;

public class Broker {
    public static void main(String[] args){
        try{
            Socket soc = new Socket("localhost", 5000);
            
            PrintWriter pr = new PrintWriter(soc.getOutputStream());
            Scanner scanner = new Scanner(System.in);
			
			String inStr = scanner.next();
			pr.println(inStr);
			pr.flush();

			scanner.close();
			soc.close();
        }
        catch(IOException err){
            err.printStackTrace();
        }
        
    }
}
