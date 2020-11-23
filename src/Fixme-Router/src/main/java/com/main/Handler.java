package com.main;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.List;

public class Handler extends Thread {
    private SocketChannel socket;
    private List<String> messages;
    private String id;
    private String componentType;
	private boolean runningClient;
	private int prt;

    public Handler(SocketChannel socket, int clientListSize, List<String> messages, int port, String id, String componentType) {
        this.socket = socket;
        this.messages = messages;
		this.id = id;
		this.prt = port;
        this.runningClient = true;
        this.componentType = componentType;
        sendMessage(id + " ");
    }

    public void sendMessage(String message) {
        try {
            if (this.runningClient) {
                ByteBuffer.wrap(message.getBytes());
                socket.write(ByteBuffer.wrap(message.getBytes()));
            } else {
                System.out.println(getClass().getSimpleName() + "Closed : " + runningClient);
            }
        } catch (IOException e) {
            System.out.println("Market not available, please connect a market");
        }
    }

    public String getMessages() {
        String ret = messages.get(0);
        updateMessages();
        return ret;
    }

    public void updateMessages() {
        messages.remove(0);
    }

    @Override
    public void run() {
        try {
        	while (this.runningClient) {
                System.out.println("In Handler.java run() loop as " + componentType);
            	if (Thread.currentThread().isAlive() == false)
        	        break;
				
                if ((socket != null) && (socket.isOpen()) && this.runningClient) {

                    ByteBuffer buffer = ByteBuffer.allocate(2048);
                    socket.read(buffer);
                    String cmsg = new String(buffer.array()).trim();
                    System.out.println("Message from : " + componentType + " ID : " + this.id + " " + cmsg);
					if (cmsg.equals("exit")){
						Main.printStr("The string is Exit in Handler.java");
						Thread.currentThread().interrupt();
						break;
					}
                    if (this.runningClient && !cmsg.isEmpty()) {
                        messages.add(cmsg);
                    }
                    buffer.flip();
                    buffer.clear();
                }
            }
		}
		catch (IOException e) {
            System.out.println("DISCONNECTED FROM " + componentType + " ID : " + this.id);
            System.out.println("SERVER IS STILL RUNNING");
        }
    }
}
