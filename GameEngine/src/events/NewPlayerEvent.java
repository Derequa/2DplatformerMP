package events;

/**
 * This class models a simple event for when a new player connects.
 * @author Derek Batts
 *
 */
public class NewPlayerEvent extends Event {

	private static final long serialVersionUID = 570891563212325873L;

	// The ID set for the new player
	public int guid;
	
	/**
	 * This makes the event.
	 * @param time The time of the event.
	 * @param priority The priority of the event.
	 * @param guid The ID given to the new player.
	 */
	public NewPlayerEvent(int time, int priority, int guid) {
		super(time, priority);
		this.guid = guid;
	}
}
