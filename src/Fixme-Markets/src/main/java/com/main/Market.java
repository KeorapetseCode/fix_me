package com.main;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.io.InputStream;
//							MARKET...............................
public class Market {
    public static void main(String[] args) {
        try {
            SocketChannel market = SocketChannel.open(new InetSocketAddress("localhost",5001));
            //market.configureBlocking(false);
			ByteBuffer buffer = ByteBuffer.allocate(1024);
			byte[] arr1 = new byte[buffer.limit()];

			InputStream in = market.socket().getInputStream();
			in.read(arr1);
			String newStr = new String(arr1);
			System.out.println("What Market Received " + newStr + "\n");

        } catch (IOException e) {
			System.out.println("Error In Market Here!!!");
            e.printStackTrace();
        }
    }
}
