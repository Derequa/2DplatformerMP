package events;

/**
 * This is a simple class to represent a death even in our game.
 * @author Derek Batts
 *
 */
public class DeathEvent extends SpawnEvent{
	
	private static final long serialVersionUID = -396472475433684516L;

	/**
	 * This makes a death event.
	 * @param time The time the event happened.
	 * @param priority The priority of the event.
	 * @param guid The GUID of the thing that died.
	 */
	public DeathEvent(int time, int priority, int guid) {
		super(time, priority, guid);
	}

}
