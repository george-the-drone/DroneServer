package edu.mit.anekin.DroneServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import edu.mit.anekin.DroneServer.Client.ClientServer;
import edu.mit.anekin.DroneServer.CommandReceiver.Command;
import edu.mit.anekin.DroneServer.CommandReceiver.CommandPasser;
import edu.mit.anekin.DroneServer.CommandReceiver.CommandServer;

public class DroneServerMain {
	
	public static int CLIENT_PORT;
	public static int COMMAND_PORT;
	
	public static String COMMAND_RECEIVER_HOST = "NOT YET ESTABLISHED";
	public static int COMMAND_RECEIVER_PORT = 0;
	
	public static BlockingQueue<Command> COMMANDS_TO_SEND = new ArrayBlockingQueue<Command>(100);
	
	public static void main(String[] args){
		if(hasValidArguments(args)){
			CLIENT_PORT = Integer.parseInt(args[0]);
			COMMAND_PORT = Integer.parseInt(args[1]);
			startCommandServer();
			startCommandPasser();
			startClientServer();
		}
	}
	
	public static void startCommandServer(){
		Thread commandServerThread = new Thread(new CommandServer());
		commandServerThread.start();
	}
	
	public static void startCommandPasser(){
		Thread commandPasserThread = new Thread(new CommandPasser());
		commandPasserThread.start();
	}
	
	public static void startClientServer(){
		Thread clientServerThread = new Thread(new ClientServer());
		clientServerThread.start();
	}
	
	public static void setDroneClientReceiverValues(String host, int port){
		COMMAND_RECEIVER_HOST = host;
		COMMAND_RECEIVER_PORT = port;
	}
	
	public static String getErrorMessage(String header){
		return header + "\n"
				+ "Correct usage:\n"
				+ "  java -jar DroneServer.jar [ClientConnectionPort] [CommandSenderPort]\n"
				+ "For example:\n"
				+ "  java -jar DroneServer.jar 40021 40022";
	}
	
	public static boolean hasValidArguments(String[] args){
		if(args.length == 2){
			int clientPort = 40021;
			int commandPort = 40022; 
			try{
				clientPort = Integer.parseInt(args[0]);
				commandPort = Integer.parseInt(args[1]);
			}catch(NumberFormatException e){
				System.out.println(getErrorMessage("Incorrect usage, both ports must be integers."));
				return false;
			}
			try{
				ServerSocket clientServer = new ServerSocket(clientPort);
				clientServer.close();
			}catch(IOException e){
				System.out.println(getErrorMessage("Could not establish connection to client port, may be taken..."));
				return false;
			}catch(IllegalArgumentException e){
				System.out.println(getErrorMessage("Client port is invalid, must be between 0 and 65535."));
				return false;
			}
			try{
				ServerSocket commandServer = new ServerSocket(commandPort);
				commandServer.close();
			}catch(IOException e){
				System.out.println(getErrorMessage("Could not establish connection to command port, may be taken..."));
				return false;
			}catch(IllegalArgumentException e){
				System.out.println(getErrorMessage("Command port is invalid, must be between 0 and 65535."));
				return false;
			}
			return true;
		}else{
			System.out.println(getErrorMessage("Incorrect usage, two ports must be specified."));
			return false;
		}
	}

}
