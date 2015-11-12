package events;

import java.io.Serializable;

public abstract class Event implements Comparable<Event>, Serializable{

	private static final long serialVersionUID = 6382042395856165794L;
	Integer timestamp;
	Integer priority;
	
	public Event(int time, int priority){
		timestamp = new Integer(time);
		this.priority = new Integer(priority);
	}
	
	public int compareTo(Event e){
		return timestamp.compareTo(e.timestamp);
	}
	
}
