package events;

/**
 * This class models a simple event for spawning an object.
 * @author Derek Batts
 *
 */
public class SpawnEvent extends Event {

	private static final long serialVersionUID = -2079299011385081720L;
	// The ID of the object to spawn
	public int guid;
	
	/**
	 * This makes the spawn event.
	 * @param time The time of the event.
	 * @param priority The priority of the event.
	 * @param guid The ID of the object to spawn.
	 */
	public SpawnEvent(int time, int priority, int guid) {
		super(time, priority);
		this.guid = guid;
	}

}
