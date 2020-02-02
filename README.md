# ElevatorSystem-SYSC3303

Overview: This Java program simulates a working elevator system based in the Dunton Tower building.

Files:
The files are divided into directories for the Floor subsystem, Scheduler subsystem, and Elevator subsystem. Each of these directories has a folder called common to keep classes that are common between all three subsystems. This will most likley include data objects that are transfered between the programs. 

Source Files:
Floor.java simulates a floor.
FloorData.java shows each line of floor data read in from a file.
Elevator.java simulates elevator.
Scheduler.java simulates elevator.
Subsystem.java contains all methods that are used for the system to send, create, recieve, and print sockets.

Installation: 
1)	Clone the project from provided GitHub link.
2)	Import the project in Eclipse IDE.
3)	Run the Floor, Elevator and Scheduler classes individually. 

Test Case:
Run the scheduler class. It will wait for a packet from the Floor class. Run the Floor class and it will send a packet to the Scheduler. Running the Elevetor class will send a packet to the Scheduler class and receive a repsonse. It will then send another packet to the Scheduler which will forward it to the Floor. This process is well defined using the Sequence Diagram.

Sequence Diagram:
https://github.com/fareedxah/ElevatorSystem-SYSC3303/blob/master/Iteration1/ElevatorSystem/SequenceDiagram.png

Class Diagram:
https://github.com/fareedxah/ElevatorSystem-SYSC3303/blob/master/Iteration1/ElevatorSystem/UML%20Class%20Diagram.png

Group Members:
1)	Jaskaran Singh 
2)	Fareed Ahmad
3)	Tareq Hanafi
4)	Shivanshi Sharma
