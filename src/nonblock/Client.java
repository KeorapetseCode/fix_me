package nonblock;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.net.InetSocketAddress;
/*
import java.nio.channels.ServerSocketChannel;
import java.net.ServerSocket;
import java.nio.channels.Selector;
import java.util.Set;
import java.nio.channels.SelectionKey;
import java.util.Iterator;
*/

public class Client {
	public static void main(String[] args) {
		try {
			String[] messages = {"I like non-blocking servers", "Hello non-blocking world!", "One more message..", "exit"};
			System.out.println("Starting client...");
			SocketChannel client = SocketChannel.open(new InetSocketAddress("localhost", 1996));

			for (String msg : messages) {
				System.out.println("Prepared message: " + msg);
				ByteBuffer buffer = ByteBuffer.allocate(1024);
				buffer.put(msg.getBytes());
				buffer.flip();
				int bytesWritten = client.write(buffer);
				System.out.println(String.format("Sending Message: %s\nbufforBytes: %d", msg, bytesWritten));
			}
			client.close();
			System.out.println("Client connection closed");
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}   
}
