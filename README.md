# ElevatorSystem-SYSC3303

Overview: This Java program simulates a working elevator system based on Dunton Tower building.

Files:
The files are divided into directories for the Floor subsystem, Scheduler subsystem, and Elevator subsystem. Each of these directories has a folder called common to keep classes that are common between all three subsystems. This will most likley include data objects that are transfered between the programs. 

Source Files:
Floor.java simulates a floor.
FloorData.java shows each line of floor data read in from a file.
Elevator.java simulates elevator.
Scheduler.java simulates elevator.
Subsystem.java contains all methods that all for the systenm to send, create, recieve, and print sockets.

Installation: 
1)	Clone the project from provided GitHub link.
2)	Import the project in Eclipse IDE
3)	Run the floor, elevator and scheduler classes individually 

Test Case:
Run the scheduler class. It will wait for a packet from the floor class. Run the floor class it will send a packt to the scheduler. Run the Elevetor class it will send a packet to the scheduler class and receive a repsonse. It will then send another packet to the scheduler which will forward it to the floor.

Group Members:
1)	Jaskaran Singh 
2)	Fareed Ahmad
3)	Tareq Hanafi
4)	Shivanshi Sharma
