/**
 * 
 */
package schedule.common;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author fareedahmad
 *
 */
public class Subsystem {
	protected void printPacket(DatagramPacket packet) {
		System.out.println("From host: " + packet.getAddress());
		System.out.println("Host port: " + packet.getPort());
		int len = packet.getLength();
		System.out.println("Length: " + len);
		System.out.print("Containing: ");

		// Form a String from the byte array.
		String received = new String(packet.getData(), 0, len);
		System.out.println(received);
	}

	protected DatagramPacket createPacket(byte[] message, int port) {
		DatagramPacket packet = null;
		// Create a packet either read or write
		try {
			packet = new DatagramPacket(message, message.length, InetAddress.getLocalHost(), port);
		} catch (UnknownHostException e) {
			e.printStackTrace();
			System.exit(1);
		}
		return packet;
	}

	protected void receivePacket(DatagramSocket receivingSocket, DatagramPacket receivingPacket,String name) {
		System.out.println(name + ": Waiting for Packet.\n");
		try {
			// Block until a datagram is received via socket.
			System.out.println("Waiting...");
			receivingSocket.receive(receivingPacket);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		System.out.println(name + ": Packet received:");
	}

	protected void sendPacket(DatagramSocket sendingSocket, DatagramPacket packet,String name) {
		System.out.println(name + ": Sending packet:");
		try {
			sendingSocket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		System.out.println(name + ": Packet sent.\n");
	}
}
