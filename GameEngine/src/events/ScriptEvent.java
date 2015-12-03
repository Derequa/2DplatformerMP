package events;

/**
 * This class wraps information for an event about an object to be handled by
 * a script in our game.
 * @author Derek Batts
 */
public class ScriptEvent extends Event {

	// Important?
	private static final long serialVersionUID = 790238686263125928L;
	// The GUID of the game object this event is about
	int guid;
	
	/**
	 * This constructs the script event.
	 * @param time The time the event happened.
	 * @param priority The priority to treat this event with.
	 * @param guid The GUID of the game object this event is tied to.
	 */
	public ScriptEvent(int time, int priority, int guid) {
		super(time, priority);
		this.guid = guid;
	}

}
