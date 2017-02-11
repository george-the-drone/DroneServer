package edu.mit.anekin.DroneServer.CommandReceiver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import edu.mit.anekin.DroneServer.DroneServerMain;

public class CommandServer implements Runnable{
	
	@Override
	public void run() {
		try {
			startServer();
		} catch (IOException e) {
			System.out.println("Error occured while handling the server");
		}
	}
	
	public void startServer() throws IOException{
		ServerSocket server = new ServerSocket(DroneServerMain.COMMAND_PORT);
		while(true){
			Socket socket = server.accept();
			handleConnection(socket);
		}
	}
	
	/**
	 * Assumes commands are proper
	 * @param socket
	 * @throws IOException
	 */
	public void handleConnection(Socket socket){
		Thread connection = new Thread(new Runnable(){
			public void run(){
				try{
					BufferedReader inputReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					String rawCommand = "";
					String line = "";
					while((line = inputReader.readLine()) != null){
						rawCommand+=line;
					}
					Command command = new Command(rawCommand);
					handleCommand(command);
					inputReader.close();
					socket.close();
				}catch(IOException e){
					System.out.println("Error occured while reading the socket");
				} catch (InterruptedException e) {
					System.out.println("Error occured while queueing the command");
				}
			}
		});
		connection.start();
	}
	
	public void handleCommand(Command command) throws InterruptedException{
		System.out.println("Command received: " + command.toString());
		DroneServerMain.COMMANDS_TO_SEND.put(command);
	}

}
