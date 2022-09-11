package com.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

public class RouterConnection extends Thread {
    private List<Handler> threadList;
    private ArrayList<String> messages = new ArrayList<String>();

    private String hostIP = "localhost";
    private int port;

    private int brokerID = 101;
    private int marketID = 505;

    private int maxBrokerCount = 50000;
    private int maxMarketCount = 100000;

    public Handler socketHandlerAsync;

    private String clienType;

    public ServerSocketChannel serverSocketChannel;
    public SocketChannel socketChannel;
    public Selector selector;

    public BufferedReader bufferedReader;

    private String componentId;

    public RouterConnection(int port, String componenType) {
        this.port = port;
        this.clienType = componenType;
        this.threadList = new ArrayList<Handler>();
    }

    public void sendMessage(String str) {
        this.socketHandlerAsync.sendMessage(str);
    }

    public String getMessages() {
        try{
          return this.socketHandlerAsync.getMessages();
        }
        catch (NullPointerException e){
            return "null";
        }
    }

    public String getComponentId() {
        return this.componentId;
    }

    private String assignIdToComponent(String componenType) {
        boolean connErr = false;
        int id = -1;

        if (componenType == "Broker") {
            id = this.brokerID++;
            if (id >= maxBrokerCount)
                connErr = true;
        }
        else if (componenType == "Market") {
            id = this.marketID++;
            if (id >= maxMarketCount)
                connErr = true;
        }
        if (connErr){
            Main.printStr("There are too many %s connections");
        }
        return Integer.toString(id);
    }

    private void runServer() {
        this.componentId = assignIdToComponent(this.clienType);
        try {
            serverSocketChannel = ServerSocketChannel.open().bind(new InetSocketAddress(this.hostIP, this.port));

            Main.printStr(String.format("Server listening on port: %d", this.port));
			socketChannel = serverSocketChannel.accept();

            socketHandlerAsync = new Handler(socketChannel, this.threadList.size(), messages, this.port, this.componentId, this.clienType);
            Main.printStr(String.format("%s connected", this.clienType));
            this.threadList.add(socketHandlerAsync);
            this.socketHandlerAsync.start();
        }
        catch (IOException e) {
            System.out.println("Disconnected");
			//.printStackTrace();
		}
    }

    @Override
    public void run() {
        runServer();
	}
}
