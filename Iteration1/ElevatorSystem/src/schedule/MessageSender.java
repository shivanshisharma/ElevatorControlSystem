package schedule;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import schedule.common.Subsystem;

public class MessageSender extends Subsystem implements Runnable {
	private byte[] message;
	private int port;
	
	private DatagramPacket instructionPacket;
	private DatagramSocket sendSocket;
	
	public MessageSender(byte[] responseMessage, int port) {
		this.message = responseMessage;
		this.port = port;
		try {
			// Construct a datagram socket to send and receive
			sendSocket = new DatagramSocket();
		} catch (SocketException se) { // Can't create the socket.
			se.printStackTrace();
			System.exit(1);
		}
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		instructionPacket = this.createPacket(message, port);
		this.printPacket(instructionPacket);
		// Send the datagram packet to the host on port 23
		this.sendPacket(sendSocket, instructionPacket, "Scheduler");
		
		sendSocket.close();
		
	}

}
