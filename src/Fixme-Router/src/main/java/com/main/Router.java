package com.main;

//import com.sun.org.apache.bcel.internal.generic.Select;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;
import java.io.PrintWriter;
import java.io.BufferedReader;
//--------------------------------ROUTER-------------------
public class Router {
    public static Selector selector = null;

    public static void main(String[] args) {
        //int ports[] = {5000, 5001};
        final String host = "localhost";

        try {
            selector = Selector.open();
			/*
			ServerSocketChannel serverChannelm = ServerSocketChannel.open();
			serverChannelm.configureBlocking(false);
			ServerSocket channelm = serverChannelm.socket();
			channelm.bind(new InetSocketAddress(host, 5001));
			serverChannelm.configureBlocking(false);
			serverChannelm.register(selector, SelectionKey.OP_ACCEPT);
			*/
			//___________________________________________________________________

			ServerSocketChannel serverChannel = ServerSocketChannel.open();
			serverChannel.configureBlocking(false);
			ServerSocket channel = serverChannel.socket();
			channel.bind(new InetSocketAddress(host, 5001));

			serverChannel.register(selector, SelectionKey.OP_ACCEPT);
			//serverChannel.register(selector, SelectionKey.OP_WRITE);
			
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
						handleRead(key, selector);
					}
					else if (key.isWritable()){
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
        channel.register(selector, SelectionKey.OP_WRITE);
		
        if (server.socket().getLocalPort() == 5000)
            System.out.println("Broker has connected, Port 5000");
        else if (server.socket().getLocalPort() == 5001)
            System.out.println("Market has connected, Port 5001");
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
				//System.out.println("Received message: " + data);
					if (data.equalsIgnoreCase("exit") == true) {
						client.close();
						System.out.println("Connection closed...");
					}
					else if (data.equalsIgnoreCase("exit") == false){
						System.out.println("Reading Message From Handle Read+: " + data);
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
		/*
		if (client.socket().getLocalPort() == 5000){
            brokerToMarket(buffer, client);
            client.register(select, SelectionKey.OP_READ);
        }
        else if (client.socket().getLocalPort() == 5001){

            client.register(select, SelectionKey.OP_READ);
		}
		*/
    }
	public static void handleWrite(SelectionKey key, Selector select){
		try{
			System.out.print("In Handle Write.....");
			SocketChannel client = (SocketChannel) key.channel();
			ByteBuffer buffer = ByteBuffer.allocate(1024);
			//System.out.println("Buffer position " + buffer.position() + "\n");
			//System.out.println("Buffer limit " + buffer.limit());
			String mesg = "Message From Server Router";
			buffer.put(mesg.getBytes());
			buffer.flip();
			client.write(buffer);
			//client.close();
			/*
			System.out.println("Buffer 2" + buffer.position() + "\n");
			System.out.println("Buffer 2" + buffer.limit());
			*/
			//String data = new String(buffer.array()).trim();
			//System.out.println("data from handleWrite: " + data);
			/*
			if (data.length() > 0){
				//System.out.println("Received message: " + data);
					if (data.equalsIgnoreCase("exit") == true) {
						client.close();
						System.out.println("Connection closed...");
					}
					else if (data.equalsIgnoreCase("exit") == false){
						System.out.println("data from Broker: " + data);
					}
			}else{
				System.out.println("There's nothing for handle write");
				client.close();
			}*/
		}
		catch(IOException erl){
			System.out.println("Error In handleWrite");
			erl.printStackTrace();
		}
	}

    public static void brokerToMarket(ByteBuffer buffer, SocketChannel channel) throws IOException{
        String msg = "Broker testing...";

        buffer.flip();
        buffer.clear();
        buffer.put(msg.getBytes());
        buffer.flip();
        buffer.rewind();
        channel.write(buffer);
    }
}
