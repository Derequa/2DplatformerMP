package events;

/**
 * This models a generic event for the replay system, as well as being
 * a notifier for starting to record a replay.
 * @author Derek Batts
 *
 */
public class ReplayEvent extends Event {

	private static final long serialVersionUID = -4886103220855434509L;

	/**
	 * This makes a ReplayEvent.
	 * @param time What time did the event happen?
	 * @param priority How important is it?
	 */
	public ReplayEvent(int time, int priority) {
		super(time, priority);
	}

}
