package events;

/**
 * This models a simple event for moving an object to a specific place in our game.
 * @author Derek Batts
 *
 */
public class MovementEvent extends Event {

	private static final long serialVersionUID = 1427383018918831936L;
	// The ID of the object to move
	public int guid;
	// The x coordinate to move to
	public int x;
	// The y coordinate to move to
	public int y;
	
	/**
	 * This makes the event.
	 * @param time The time of the event.
	 * @param priority The priority of the event.
	 * @param guid The ID of the object to move.
	 * @param x The x coordinate to move to.
	 * @param y The y coordinate to move to.
	 */
	public MovementEvent(int time, int priority, int guid, int x, int y) {
		super(time, priority);
		this.guid = guid;
		this.x = x;
		this.y = y;
	}

}
