# fix_me
  Stock market simulation program built in Java. <br />
  This project is an introduction to multi-threading, maven and java networking.<br />
  <br />
# Objective <br />
Build a robust and performant messaging platform with three seperate components which will be communicting with each other through specific network protocol. <br />
The three components are Router, Market and Broker. <br />
The Router is the central component of the entire application. All other components connect to it in order to send messages to other components. Messages will be in FIX format. <br />
The Broker will send two types of messages:
• Buy. - An order where the broker wants to buy an instrument
• Sell. - An order where the broker want to sell an instrument
<br /> 
The Market has a list of instruments that can be traded. When orders are received from brokers the market tries to execute it.
# Rules
 - Build the entire project with one mvn file that is found at the root of the project. <br />
 - Each component can have its own mvn file found at each componenents root directory.<br />
# How To Run
 - $mvn clean package <br />
 - After building, I have written a shell script to run each component independently. Each component will be ran on its own terminal window <br />
 <br />

# Prerequisites
  This is a command line program. javac, maven and java is to be installed prior.
