package com.main;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.io.InputStream;

//							MARKET...............................
public class Market {
    public static void main(String[] args) {
        try {
            SocketChannel market = SocketChannel.open(new InetSocketAddress("localhost",4001));
			market.configureBlocking(false);
			
			ByteBuffer buffer = ByteBuffer.allocate(1024);
			//String data = new String(buffer.array()).trim();
			//buffer.flip();
			//System.out.println("Market is " + data);
			while (true){
				market.read(buffer);
				String data = new String(buffer.array()).trim();
				if (data.isBlank()){
					//System.out.println("Market is Blank");
			//		continue ;
				}
				else{
					System.out.println("Reading In Market " + data);
					if (data.equals("exit"))
						break ;
				}
			}
			//PrintWriter pr = new PrintWriter(client.socket().getInputStream());
			//String msg = "String from market[[[[[[[[]]]]]]]]";
			
			//
			//int bytesWritten = market.write(buffer);
			//System.out.println("Market is closing!!!");
			//market.close();
        } catch (IOException e) {
			System.out.println("Error In Market Here!!!");
            e.printStackTrace();
        }
    }
}
