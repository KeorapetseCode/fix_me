package com.main;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Random;
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
    
        SocketChannel socketChannel = (SocketChannel) key.channel();
        byteBuffer = ByteBuffer.allocate(1024);

        socketChannel.read(byteBuffer);
        String routerOutput = new String(byteBuffer.array()).trim();
        if (ID.isEmpty()) {
            ID = routerOutput;
            System.out.println(" Market ID: " + routerOutput);
        }
        else {
			if (routerOutput.equals("exit")){
                socketChannel.close();
                System.exit(0);
                return ;
			}
            handleRequest(key, routerOutput);
        }
    }

    public static void handleRequest(SelectionKey key, String brokerRequest) {
        
        SocketChannel socketChannel = (SocketChannel) key.channel();
        String[] requestSplit = brokerRequest.split("\\|");
        String choice = requestSplit[1];

        String[] requestStatus = {"accepted", "rejected"};
        Random random = new Random();
        String temp = requestStatus[random.nextInt(2)];
        if (choice.equals("1") && temp.equals("accepted"))
            gold--;
		else if (choice.equals("2") && temp.equals("accepted"))
            gold++;

        String marketReturn = temp + "|" + brokerRequest;
        byteBuffer = ByteBuffer.wrap(marketReturn.getBytes());
        try {
            socketChannel.write(byteBuffer);
            printStr("Message from market: " + marketReturn + "\n Completed");
            printStr("Gold Stock: " + gold);
        }
        catch (IOException e) {
            printStr("request failed");
        }
	}
	
    public static void printStr(String s){
        System.out.println(s); 
	}
}
