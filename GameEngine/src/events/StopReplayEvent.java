package events;

/**
 * This class models a simple event for signaling stopping the recording of a replay.
 * @author Derek Batts
 *
 */
public class StopReplayEvent extends ReplayEvent {

	private static final long serialVersionUID = -8154059349516412773L;

	/**
	 * This makes the event.
	 * @param time The time of the event.
	 * @param priority The priority of the event.
	 */
	public StopReplayEvent(int time, int priority) {
		super(time, priority);
	}

}
