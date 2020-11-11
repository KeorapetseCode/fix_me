package com.main;

//import com.sun.org.apache.bcel.internal.generic.Select;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;
//import java.io.PrintWriter;
//import java.io.BufferedReader;
import java.lang.Thread;

//--------------------------------ROUTER-------------------
public class Router extends Thread {
	
    public static Selector selector = null;

    public static void main(String[] args) {
        final String host = "localhost";

        try {
            selector = Selector.open();
			
			ServerSocketChannel serverChannelm = ServerSocketChannel.open();
			serverChannelm.configureBlocking(false);
			ServerSocket channelm = serverChannelm.socket();
			channelm.bind(new InetSocketAddress(host, 4000));
			serverChannelm.register(selector, SelectionKey.OP_ACCEPT);
			//___________________________________________________________________

			ServerSocketChannel serverChannel = ServerSocketChannel.open();
			serverChannel.configureBlocking(false);
			ServerSocket channel = serverChannel.socket();
			channel.bind(new InetSocketAddress(host, 4001));
			serverChannel.register(selector, SelectionKey.OP_ACCEPT);
			
			System.out.println("Router ready for connection...");
            while (true){
				selector.select();
				Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> keys = selectedKeys.iterator();
				
				while (keys.hasNext()){
                    SelectionKey key = keys.next();
                    if (key.isAcceptable()){
                        connectedPort(key, selector);
					}
					else if (key.isReadable()){
						System.out.println("In key.readLLLL");
						handleRead(key, selector);
					//	handleWrite(key, selector);
					}
					else if (key.isWritable()){
						System.out.println("In key.writable");
						handleWrite(key, selector);
						//System.out.println("Writable From Server@@@@@@@@@@@@@ " + "\n");
					}
					keys.remove();
				}
            }
		}
		catch (IOException e) {
			System.out.println("Error In Router______");
			e.printStackTrace();
        }
    }

    public static void connectedPort(SelectionKey key, Selector selector) throws IOException {
        System.out.println("Connecting From connectPort...");
		
		ServerSocketChannel server = (ServerSocketChannel) key.channel();
		
		SocketChannel channel = server.accept();
        channel.configureBlocking(false);

        if (server.socket().getLocalPort() == 4000){
			System.out.println("Broker has connected, Port 4000");
			channel.register(selector, SelectionKey.OP_READ);
		}
        else if (server.socket().getLocalPort() == 4001){
			System.out.println("Market has connected, Port 4001");
			channel.register(selector, SelectionKey.OP_WRITE);
		}
	}

    public static void handleRead(SelectionKey key, Selector select){
		try{
			System.out.println("Reading...");
			SocketChannel client = (SocketChannel) key.channel();
			client.configureBlocking(false);
			ByteBuffer buffer = ByteBuffer.allocate(1024);

			client.read(buffer);
			String data = new String(buffer.array()).trim();
			if (data.length() > 0){
					if (data.equalsIgnoreCase("exit") == true) {
						client.close();
						System.out.println("Connection closed...");
					}
					else if (data.equalsIgnoreCase("exit") == false){
						System.out.println("Reading Message From HandleRead: " + data);
						//if (data.equals("buy") || data.equals("sell"))
							//client.register(selector, SelectionKey.OP_WRITE);
					}
			}
			else{
				System.out.println("There's nothing");
				client.close();
			}
		}
		catch(IOException err){
			System.out.println("Error In handle read");
			err.printStackTrace();
		}
    }
	public static void handleWrite(SelectionKey key, Selector select){
		try{
			System.out.print("In Handle Write....." + "\n");
			SocketChannel client = (SocketChannel) key.channel();
			ByteBuffer buffer = ByteBuffer.allocate(1024);
/*			
			System.out.println("Buffer 2" + buffer.position() + "\n");
			System.out.println("Buffer 2" + buffer.limit());
*/
			client.write(buffer);
			//client.close();
		}
		catch(IOException erl){
			System.out.println("Error In handleWrite");
			erl.printStackTrace();
		}
	}
/*
    public static void brokerToMarket(ByteBuffer buffer, SocketChannel channel) throws IOException{
        String msg = "Broker testing...";

        buffer.flip();
        buffer.clear();
        buffer.put(msg.getBytes());
        buffer.flip();
        buffer.rewind();
        channel.write(buffer);
    }*/
}
