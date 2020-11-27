package com.main;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class Main {
    private static ByteBuffer byteBuffer = null;

    static String message = null;

    public static int gold = 100;
    public static String ID = "";

    public static final String host = "localhost";
    public static final int port = 5001;

    public static void main(String[] args) throws Exception {

        Selector selector = Selector.open();
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);
        socketChannel.connect(new InetSocketAddress(host, port));
        socketChannel.register(selector, SelectionKey.OP_CONNECT | SelectionKey.OP_READ | SelectionKey.OP_WRITE);

        while (true) {
            if (selector.select() > 0) {
                Iterator <SelectionKey> i = selector.selectedKeys().iterator();
                SelectionKey key = null;

                while (i.hasNext()) {
                    key = (SelectionKey) i.next();
                    i.remove();
                }
                if (processKey(key)) {
                    break;
                }
            }
        }
        socketChannel.close();
    }

    public static Boolean processConnection(SelectionKey key) {

        SocketChannel socketChannel = (SocketChannel) key.channel();
        try {
            while (socketChannel.isConnectionPending()) {
                socketChannel.finishConnect();
            }
        } catch (IOException e) {
            key.cancel();
            return false;
        }
        return true;
    }

    public static Boolean processKey(SelectionKey key) throws Exception {
        if (key.isConnectable()) {
            if (!processConnection(key)) {
                return true;
            }
        }
        if (key.isReadable()) {
            readableKey(key);
        }
        return false;
    }

    public static void readableKey(SelectionKey key) throws IOException {
        // get channel associated with this key
        SocketChannel socketChannel = (SocketChannel) key.channel();
        // initialise buffer to read from channel
        byteBuffer = ByteBuffer.allocate(1024);

        // very first message is the ID sent by router
        socketChannel.read(byteBuffer);
        String routerOutput = new String(byteBuffer.array()).trim();

        // if ID is not set, then this is the first message received ie.:ID
        if (ID.isEmpty()) {
            ID = routerOutput;
            System.out.println(" Market ID: " + routerOutput);

        } else {
			// this is a message from the broker (request)
			if (routerOutput.equals("exit")){
				printStr("EXIT in readable!!!");
				return ;
			}
            System.out.println(" Message from broker: " + routerOutput);
            handleRequest(key, routerOutput);
        }
    }

    public static void handleRequest(SelectionKey key, String brokerRequest) {
        // get the channel associated with this key
        SocketChannel socketChannel = (SocketChannel) key.channel();
        // split the brokerRequest string (currently in fix format)
        String[] requestSplit = brokerRequest.split("\\|");
        // get broker request (buy/sell)
        String choice = requestSplit[2];

        // pre-allocate brokerRequest status (if market is able/unable to fulfill request)
        String requestStatus = "accepted";
        if (choice.equals("1"))
            gold--;
		else 
            gold++;

        String marketReturn = requestStatus + "|" + brokerRequest;
        byteBuffer = ByteBuffer.wrap(marketReturn.getBytes());

        try {
            socketChannel.write(byteBuffer);
            printStr("Message from market: " + marketReturn + "\n Completed");
            printStr("Gold Asset: " + gold);
        }
        catch (IOException e) {
            printStr("request failed");
        }
	}
	
    public static void printStr(String s){
        System.out.println(s); 
	}
}
