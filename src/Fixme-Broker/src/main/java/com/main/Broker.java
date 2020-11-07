package com.main;

//import com.sun.tools.jdeprscan.scan.Scan;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Scanner;
import java.io.PrintWriter;
//								BROKER Is Client 1.........................
public class Broker {
    public static void main(String[] args) {
		try {
			
			SocketChannel client = SocketChannel.open(new InetSocketAddress("localhost", 5000));
			client.configureBlocking(false);
			//PrintWriter pr = new PrintWriter(client.socket().getOutputStream());
			Scanner scanner = new Scanner(System.in);
			String inStr = null;

			while (scanner.hasNextLine()){

				inStr = scanner.next();
				System.out.println("Prepared message: " + inStr);
				ByteBuffer buffer = ByteBuffer.allocate(1024);
				buffer.put(inStr.getBytes());
				buffer.flip();
				int bytesWritten = client.write(buffer);
				if (inStr.equals("exit"))
					break ;
				//System.out.println(String.format("Sending Message from client 1: %s\nbufforBytes: %d", inStr, bytesWritten));
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
