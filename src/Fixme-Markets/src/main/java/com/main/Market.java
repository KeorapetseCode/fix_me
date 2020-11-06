package com.main;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.io.PrintStream;
//							MARKET...............................
public class Market {
    public static void main(String[] args) {
        try {
            SocketChannel market = SocketChannel.open(new InetSocketAddress("localhost",5001));
            //market.configureBlocking(false);
			ByteBuffer buffer = ByteBuffer.allocate(1024);
			
			
			
			


        } catch (IOException e) {
			System.out.println("Error In Market Here!!!" + "\n");
            e.printStackTrace();
        }
    }
}
