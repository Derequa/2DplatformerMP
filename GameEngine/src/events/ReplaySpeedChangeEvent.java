package events;

/**
 * This models an event for changing the speed of the replay.
 * @author Derek Batts
 *
 */
public class ReplaySpeedChangeEvent extends ReplayEvent {

	private static final long serialVersionUID = -1306998277039799173L;

	// The new speed to remember/set
	public int newSpeed;
	
	/**
	 * This constructs the event.
	 * @param time The time this event was made.
	 * @param priority The priority to give this event.
	 * @param newSpeed The new speed to set for replaying.
	 */
	public ReplaySpeedChangeEvent(int time, int priority, int newSpeed) {
		super(time, priority);
		this.newSpeed = newSpeed;
	}

}
