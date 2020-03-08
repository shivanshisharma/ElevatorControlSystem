package schedule;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;

import schedule.common.Elevator;
import schedule.common.ElevatorMessage;
import schedule.common.Subsystem;

public class ElevatorThread extends Subsystem implements Runnable{
	private DatagramPacket elevatorPacket, responsePacket;
	private DatagramSocket elevatorSocket;
	private Scheduler scheduler;

	public ElevatorThread(Scheduler scheduler, DatagramSocket socket) {
		this.scheduler = scheduler;
		this.elevatorSocket = socket;
		// Create socket to send and receive
		/*
		try {
			elevatorSocket = new DatagramSocket(23);
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
			byte elevatorData[] = new byte[10];
			elevatorPacket = new DatagramPacket(elevatorData, elevatorData.length);
			this.receivePacket(elevatorSocket, elevatorPacket,"ElevatorThread");
			
			scheduler.addElevatorMessage(new ElevatorMessage(elevatorPacket.getPort(), elevatorData[0],elevatorData[1],elevatorData[2],elevatorData[3],elevatorData[4]));
			// Print out received packet
			//this.printPacket(elevatorPacket);
		}
	}
}
