# DroneServer
The main program that runs on a user's computer, communicating between the computer and the drone.

# Usage
Run the jar file as follows:

`java -jar DroneServer.jar [ClientConnectionPort] [CommandSenderPort]`

For example: `java -jar DroneServer.jar 40021 40022`

Make sure to run DroneServer before starting up DroneClient or sending commands with DroneCommandSender.
