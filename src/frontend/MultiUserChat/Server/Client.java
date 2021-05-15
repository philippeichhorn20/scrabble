package frontend.MultiUserChat.Server;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
/** @author vivanova
* @version 1.0
*/

public class Client {

	private DatagramSocket socket;
	private InetAddress address;
	private int port;
	
	private boolean running;
	private String name;
	

	// gets name from profile, address and port
	public Client(String name,String address, int port) {
		try {
			this.name = name;
			this.address = InetAddress.getByName(address);
			this.port = port;
			
			socket = new DatagramSocket();
			
			running = true;
			listen();
			send("\\con:" + name);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	public void send(String message) {
		try {
			if(!message.startsWith("\\")) {
				message = name + ": " + message;
			}
			message += "\\e";
			byte[] data = message.getBytes();
			DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
			socket.send(packet);
			System.out.println("Sent Message to: " + address.getHostAddress() + ":" + port);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void listen() {
		Thread listenThread = new Thread("ChatProgram Listener") {
			public void run() {

				try {
					while (running) {
						byte[] data = new byte[1024];
						DatagramPacket packet = new DatagramPacket(data, data.length);
						socket.receive(packet);

						// Marks the end of a message
						String message = new String(data);
						message = message.substring(0, message.indexOf("\\e"));

						// MANAGE MESSAGE
						if (!isCommand(message, packet)) {

							//Print Message
							ClientWindow.printToConsole(message);				
							
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		};
		listenThread.start();

	}
	private static boolean isCommand(String message, DatagramPacket packet) {

		if (message.startsWith("\\con:")) {
			// RUN CONNECTION CODE


			return true;
		}

		return false;
	}

}
