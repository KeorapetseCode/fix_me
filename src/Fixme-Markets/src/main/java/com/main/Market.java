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
            SocketChannel market = SocketChannel.open(new InetSocketAddress("localhost",5001));
			market.configureBlocking(false);
			ByteBuffer buffer = ByteBuffer.allocate(1024);
			//buffer.flip();
			market.read(buffer);
			
			String data = new String(buffer.array()).trim();
			System.out.println("Reading In Market " + data);

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
