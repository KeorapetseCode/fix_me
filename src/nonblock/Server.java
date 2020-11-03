package nonblock;

import java.io.IOException;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.net.ServerSocket;
import java.nio.channels.Selector;
import java.net.InetSocketAddress;
import java.util.Set;
import java.nio.channels.SelectionKey;
import java.util.Iterator;
import java.nio.ByteBuffer;

public class Server {
    
    static Selector selector = null;

    public static void main(String[] args){						
        try{
			ServerSocketChannel socketChnl = null;
			ServerSocket serverSocket = null;
			
			selector = Selector.open();
            socketChnl = ServerSocketChannel.open();
            serverSocket = socketChnl.socket();
			
			serverSocket.bind(new InetSocketAddress("localhost", 1996));
			socketChnl.configureBlocking(false);
			
			int ops = socketChnl.validOps();//System.out.println("OPS int " + ops + "\n");
			socketChnl.register(selector, ops, null);

			while (true) {
                selector.select();
                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> iter = selectedKeys.iterator();
				/*
                while (iter.hasNext()) {
                    SelectionKey key = iter.next();	
					//System.out.println();
					if (key.isAcceptable()) {
//                        New client has been accepted
                        handleAccept(socketChnl, key);
					}
					else if (key.isReadable()) {
//                        We can run non-blocking operation READ on our client
                        handleRead(key);
                    }
                    
				}*/
				while (iter.hasNext()){
					System.out.println("selected keys STRING! " + "\n" + iter.next());
					iter.remove();
				}
			}
		}
        catch (IOException err){
            System.out.println("There's some Error");
            err.printStackTrace();
        }
	}
	private static void handleAccept(ServerSocketChannel mySocket, SelectionKey key){

		System.out.println("Connection Accepted...");
		try{
// Accept the connection and set non-blocking mode
			SocketChannel client = mySocket.accept();
			client.configureBlocking(false);
// Register that client is reading this channel
			client.register(selector, SelectionKey.OP_READ);
		}
		catch (IOException err){
			System.out.println("handleAccept Error!!!!!" + "\n");
			err.printStackTrace();
		}
	}

	private static void handleRead(SelectionKey selKey){
		try{
			// create a ServerSocketChannel to read the request
			SocketChannel client = (SocketChannel) selKey.channel();
			// Create buffer to read data
			ByteBuffer buffer = ByteBuffer.allocate(1024);
			client.read(buffer);
	//        Parse data from buffer to String
			String data = new String(buffer.array()).trim();
			if (data.length() > 0) {
				System.out.println("Received message: " + data);
				if (data.equalsIgnoreCase("exit")) {
					client.close();
					System.out.println("Connection closed...");
				}
			}
		}
		catch (IOException err){
			System.out.println("Error In handle read");
			err.printStackTrace();
		}
	}
}
