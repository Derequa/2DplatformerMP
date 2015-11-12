package events;

import java.io.Serializable;

/**
 * This class models a generic event in our game.
 * @author Derek Batts
 *
 */
public abstract class Event implements Comparable<Event>, Serializable{

	private static final long serialVersionUID = 6382042395856165794L;
	// What time was this event made
	Integer timestamp;
	// How soon should this event be handled
	Integer priority;
	
	/**
	 * This defines the generic constructor for an event.
	 * @param time The time the event happened.
	 * @param priority The priority of the event.
	 */
	public Event(int time, int priority){
		timestamp = new Integer(time);
		this.priority = new Integer(priority);
	}
	
	/**
	 * This implements event comparison for sorting in our event queues.
	 */
	public int compareTo(Event e){
		return timestamp.compareTo(e.timestamp);
	}
	
}
