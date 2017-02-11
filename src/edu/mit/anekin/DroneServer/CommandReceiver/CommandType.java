package edu.mit.anekin.DroneServer.CommandReceiver;

public enum CommandType {
	
	BEHAVIOR("behavior"),
	ACTION("action");
	
	private final String name;
	
	private CommandType(String name){
		this.name = name;
	}
	
	public String getName(){
		return name;
	}
	
	public static CommandType getCommandTypeFromName(String name){
		switch(name.toLowerCase()){
			case "behavior":
				return BEHAVIOR;
			case "action":
				return ACTION;
			default:
				return BEHAVIOR;
		}
	}

}
