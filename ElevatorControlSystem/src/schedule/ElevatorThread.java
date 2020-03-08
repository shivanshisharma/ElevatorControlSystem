package schedule;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import schedule.common.ElevatorMessage;
import schedule.common.Subsystem;

public class ElevatorThread extends Subsystem implements Runnable{
	private DatagramPacket elevatorPacket;
	private DatagramSocket elevatorSocket;
	private Scheduler scheduler;

	public ElevatorThread(Scheduler scheduler, DatagramSocket socket) {
		this.scheduler = scheduler;
		this.elevatorSocket = socket;
	}

	@Override
	public void run() {
		while (true) {
			// Receive packet
			byte elevatorData[] = new byte[10];
			elevatorPacket = new DatagramPacket(elevatorData, elevatorData.length);
			this.receivePacket(elevatorSocket, elevatorPacket,"ElevatorThread");
			
			scheduler.addElevatorMessage(new ElevatorMessage(elevatorPacket.getPort(), elevatorData[0],elevatorData[1],elevatorData[2],elevatorData[3],elevatorData[4]));
		}
	}
}
