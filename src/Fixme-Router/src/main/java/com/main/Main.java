package com.main;

public class Main {
    public static final int brokerPort = 5000;
    public static final int marketPort = 5001;

    private static String brokerMessages = "";
    private static String marketMessages = "";

    public static void printStr(String s){
        System.out.println(s);
	}

    public static void main(String[] args) {

        RouterConnection brokerServer = new RouterConnection(brokerPort, TheComp.Broker);
        RouterConnection marketServer = new RouterConnection(marketPort, TheComp.Market);

        brokerServer.start();
        marketServer.start();

        while (true) {
            brokerMessages = brokerServer.getMessages();
            if (brokerMessages != "null"){
				try {
						if (brokerMessages.equals("exit")){

							marketServer.sendMessage(brokerMessages);

							brokerServer.socketHandlerAsync.interrupt();
							marketServer.socketHandlerAsync.interrupt();
							brokerServer.interrupt();
							marketServer.interrupt();
							break ;
						}
						String[] arr = brokerMessages.split("\\|");
						String targetID = marketServer.getComponentId();
						String brokerMessageTargeted = arr[0] + "|" + targetID + "|" + arr[2] + "|" + arr[3] + "|";
						marketServer.sendMessage(brokerMessageTargeted);
						brokerMessages = "null";

						marketMessages = marketServer.getMessages();
						brokerServer.sendMessage(marketMessages);
						marketMessages = "null";
						System.out.println("Order processed");
				}
				catch (Exception e) {
					printStr("In Router try catch");
					e.printStackTrace();
				}
			}
		}
    }
}
