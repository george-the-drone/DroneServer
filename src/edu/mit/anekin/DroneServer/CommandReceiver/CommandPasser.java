package edu.mit.anekin.DroneServer.CommandReceiver;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import edu.mit.anekin.DroneServer.DroneServerMain;

public class CommandPasser implements Runnable{

	@Override
	public void run() {
		while(true){
			//Check to see if connection has been established
			if(DroneServerMain.COMMAND_RECEIVER_HOST.equals("NOT YET ESTABLISHED")){
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					System.out.println("Error occured with passer sleeping while waiting for connection");
				}
			}else{
				System.out.println("Client connection found, beginning command passer");
				break;
			}
		}
		try {
			startCommandSending();
		} catch (IOException | InterruptedException e) {
			System.out.println("Error establishing connection to the drone client for message passing");
		}
	}
	
	public void startCommandSending() throws UnknownHostException, IOException, InterruptedException{
		while(true){
			Socket socket = new Socket(DroneServerMain.COMMAND_RECEIVER_HOST, DroneServerMain.COMMAND_RECEIVER_PORT);
			PrintWriter output = new PrintWriter(socket.getOutputStream());
			Command command = DroneServerMain.COMMANDS_TO_SEND.take();
			output.println(command.toString());
			output.flush();
		}
	}

}
