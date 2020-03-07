package schedule;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import schedule.common.Floor;
import schedule.common.Subsystem;

public class FloorThread extends Subsystem implements Runnable {
	private DatagramPacket floorPacket, responsePacket;
	private DatagramSocket floorSocket;
	private Scheduler scheduler;

	public FloorThread(Scheduler scheduler, DatagramSocket socket) {
		this.scheduler = scheduler;
		this.floorSocket = socket;
		// Create socket to send and receive
		/*
		try {
			floorSocket = new DatagramSocket(23);
		} catch (SocketException se) {
			se.printStackTrace();
			System.exit(1);
		}
		*/
	}

	@Override
	public void run() {
		while (true) {
			// Receive packet
			byte floorData[] = new byte[100];
			floorPacket = new DatagramPacket(floorData, floorData.length);
			this.receivePacket(floorSocket, floorPacket,"FloorThread");
			scheduler.addFloorRequest(new Floor(5, true, 8));
			// Print out received packet
			this.printPacket(floorPacket);
		}
	}

}
