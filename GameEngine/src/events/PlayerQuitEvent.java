package events;

/**
 * This models a simple event for when a player quits or disconnects.
 * @author Derek Batts
 *
 */
public class PlayerQuitEvent extends Event {

	private static final long serialVersionUID = 6365457343803170693L;

	// The ID of the player
	public int guid;
	
	/**
	 * This makes the event.
	 * @param time The time of the event.
	 * @param priority The priority of the event.
	 * @param guid The ID of the player that quit/disconnected.
	 */
	public PlayerQuitEvent(int time, int priority, int guid) {
		super(time, priority);
		this.guid = guid;
	}

}
