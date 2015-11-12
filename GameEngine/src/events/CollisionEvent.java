package events;

/**
 * This class models a simple event about a collision between two
 * game objects.
 * @author Derek Batts
 *
 */
public class CollisionEvent extends Event {
	
	private static final long serialVersionUID = -8289416432572582366L;
	// GUIDs for the two game objects
	public int guid1;
	public int guid2;

	/**
	 * This makes a CollisionEvent.
	 * @param time The time the event occurred.
	 * @param priority The priority of the event.
	 * @param guid1 The GUID of one of the objects involved in the collision.
	 * @param guid2 The GUID of the other object involved in the collision.
	 */
	public CollisionEvent(int time, int priority, int guid1, int guid2) {
		super(time, priority);
		this.guid1 = guid1;
		this.guid2 = guid2;
	}
}
