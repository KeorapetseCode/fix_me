package com.main;

//import com.sun.tools.jdeprscan.scan.Scan;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Scanner;
//import com.main.Transaction;
//								BROKER Is Client 1.........................
public class Broker {
    public static void main(String[] args) {
		
		try {
			SocketChannel client = SocketChannel.open(new InetSocketAddress("localhost", 4000));
			client.configureBlocking(false);
			Scanner scanner = new Scanner(System.in);
			String inStr = null;
			while (true){
				inStr = scanner.next();
				
				ByteBuffer buffer = ByteBuffer.allocate(1024);
				buffer.put(inStr.getBytes());
				buffer.flip();
				int bytesWritten = client.write(buffer);
				if (inStr.equals("exit"))
					break ;
				System.out.println(String.format("Sending Message from Broker: %s\nbufforBytes: %d", inStr, bytesWritten));
			}
			//pr.println(inStr);
			scanner.close();
			client.close();
			System.out.println("Client 1 connection closed!!!!");
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

    public static void getRespondsFromServer(String msg, SocketChannel channel) throws IOException{
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        buffer.flip();
        buffer.clear();
        buffer.put(msg.getBytes());
        buffer.flip();
        channel.write(buffer);
        Selector selector = Selector.open();
        channel.register(selector, SelectionKey.OP_READ);
    }
}
