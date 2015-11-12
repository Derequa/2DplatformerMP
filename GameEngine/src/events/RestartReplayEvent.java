package events;

/**
 * This is a simple event to signal restarting a replay.
 * @author Derek Batts
 *
 */
public class RestartReplayEvent extends ReplayEvent {

	private static final long serialVersionUID = -1958326140375860798L;

	/**
	 * This creates the event.
	 * @param time The time of the event.
	 * @param priority The priority of the event.
	 */
	public RestartReplayEvent(int time, int priority) {
		super(time, priority);
	}

}
