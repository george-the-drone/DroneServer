package edu.mit.anekin.DroneServer.Client;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

import javax.imageio.ImageIO;

import edu.mit.anekin.DroneServer.DroneServerMain;

public class ClientServer implements Runnable{
	
	int imageCount = 0;

	@Override
	public void run() {
		try {
			startServer();
		} catch (IOException e) {
			System.out.println("Error occured while starting the client server");
		}
	}
	
	public void startServer() throws IOException{
		ServerSocket server = new ServerSocket(DroneServerMain.CLIENT_PORT);
		while(true){
			Socket socket = server.accept();
			handleConnection(socket, imageCount);
			imageCount++;
		}
	}
	
	public void handleConnection(Socket socket, int imageNum){
		Thread connection = new Thread(new Runnable(){
			public void run(){
				try {
					if(DroneServerMain.COMMAND_RECEIVER_HOST.equals("NOT YET ESTABLISHED")){
						BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
						String host = reader.readLine();
						int port = Integer.parseInt(reader.readLine());
						reader.close();
						socket.close();
						DroneServerMain.setDroneClientReceiverValues(host, port);
					}else{
						BufferedImage image = ImageIO.read(socket.getInputStream());
						socket.close();
						ImageIO.write(image, "jpg", new File("images/image" + imageNum + ".jpg"));
						
					}
					/*System.out.println("Received connection");
					ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
					System.out.println("Test231");
					ClientMessage message = (ClientMessage) input.readObject();
					InputStream stream = new ByteArrayInputStream(message.getBytes());
					BufferedImage image = ImageIO.read(stream);
					ImageIO.write(image, ".jpg", new File("image" + imageNum + ".jpg"));
					System.out.println("Test1323123");
					System.out.println(message.getHost() + ":" + message.getPort());
					DroneServerMain.setDroneClientReceiverValues(message.getHost(), message.getPort());
					System.out.println("Made it");
					input.close();
					socket.close();*/
				} catch (IOException e) {
					System.out.println("Error occured while handling client image socket");
				}
			} 
		});
		connection.start();
	}

}
