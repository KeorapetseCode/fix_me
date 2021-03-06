package com.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.zip.CRC32;

public class Main {
    private static BufferedReader bufferedReader = null;
    private static ByteBuffer byteBuffer = null;
    protected SocketChannel client;
    protected ArrayList<String> messages = new ArrayList<>();
    public static final String host = "localhost";
    public static final int port = 5000;

    public static String ID = "";
	public static int targetID = 0;
//	public static int cancel = 0;

    private static int gold;
    public static int choice = 1;
    private static int money = 100;

    public static void main(String[] args) throws Exception {

        Selector selector = Selector.open();
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);
        socketChannel.connect(new InetSocketAddress(host, port));

        socketChannel.register(selector, SelectionKey.OP_CONNECT | SelectionKey.OP_READ | SelectionKey.OP_WRITE);
        bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        
        while (true) {
            //printStr("In Broker Loop 1");
            if (selector.select() > 0) {
                Iterator <SelectionKey> i = selector.selectedKeys().iterator();
                SelectionKey key = null;
                while (i.hasNext()) {
          //          printStr("In Broker Loop 2");
                    key = (SelectionKey) i.next();
                    i.remove();
                }
                if (processKey(key)){
                    System.out.println("Breaking!!!!! from processKey");
                    break;
				}
			//	else if(cancel == 3)
			//		break;
            }
        }
        socketChannel.close();
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

    public static void printStr(String s){
        System.out.println(s);
    }

    public static void readableKey(SelectionKey key) throws IOException {
        SocketChannel socketChannel = (SocketChannel) key.channel();
        byteBuffer = ByteBuffer.allocate(1024);
        socketChannel.read(byteBuffer);
        String routerOutput = new String(byteBuffer.array()).trim();

        if (ID.isEmpty()) {
            ID = routerOutput;
            System.out.println(" BroKer ID: " + routerOutput);
		}
		else{
            //System.out.println(" Server response: " + routerOutput);
            //printStr("Rpouter Output!!" + routerOutput);
            setAsset(routerOutput);
		}
		Menu(socketChannel);
    }

    public static Boolean processConnection(SelectionKey key) {
        SocketChannel socketChannel = (SocketChannel) key.channel();
        try {
            while (socketChannel.isConnectionPending()) {
                socketChannel.finishConnect();
            }
		}
		catch (IOException e) {
            key.cancel();
            return false;
        }
        return true;
    }

    public static void Menu(SocketChannel socketChannel) {
        while (true) {
            try {
                printStr(" Would you like to buy or sell units of gold? \n Enter : 1 to buy or 2 to sell ");

                String input = bufferedReader.readLine();

                if (input.equalsIgnoreCase("1") && money > 19){
                    input = setFix(input);
					socketChannel.write(ByteBuffer.wrap(input.getBytes()));
                    money = money - 20;
					return;
                }
                else if (input.equalsIgnoreCase("2")) {
                    input = setFix(input);
                    socketChannel.write(ByteBuffer.wrap(input.getBytes()));
                    money = money + 20;
                    return;
                }
                else if(input.equalsIgnoreCase("exit")){
                    printStr("Closing Application");
                    socketChannel.write(ByteBuffer.wrap(input.getBytes()));
				    socketChannel.close();
                    System.exit(0);
                }
                else{
                    printStr("Invalid String");
                }
            }
            catch (IOException e) {
                System.out.println("IO Exception caught: " + e);
            }
        }
    }

    public static String setFix(String choice) {
        String fixed = ID + "|" + choice + "|" + checksumCreate(choice) + "|";
        return (fixed);
    }

    public static long checksumCreate(String fixedBody) {
        byte[] bytes = fixedBody.getBytes();
        CRC32 crc32 = new CRC32();
        crc32.update(bytes, 0, bytes.length);
        return crc32.getValue();
    }

    public static void setAsset(String routerOutput) {

        String[] routerOutputSplit = routerOutput.split("\\|");

        //printStr("router Meassage " + routerOutputSplit[0]);
        if (routerOutputSplit[0].equals("accepted")) {
            if (routerOutputSplit[3].equals("1")) 
                gold++;
            else if (routerOutputSplit[3].equals("2"))
                gold--;
            printStr("Previous Order Was Successful");
        }
        else if (routerOutputSplit[0].equals("rejected")) {
            printStr("Previous Order Was Unsuccessful ");
        }
        if (gold > 0)
            printStr("Personal Gold Asset" + gold);
    }
}