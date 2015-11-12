package events;

/**
 * This models a simple event for playing/pausing the replay.
 * @author Derek Batts
 *
 */
public class PlayPauseReplayEvent extends ReplayEvent {

	private static final long serialVersionUID = 8199417468197397810L;

	// A flag for whether to play or pause the event
	public boolean play;
	
	/**
	 * This makes the event.
	 * @param time The time of the event.
	 * @param priority The priority of the event.
	 * @param play True to play, false to pause.
	 */
	public PlayPauseReplayEvent(int time, int priority, boolean play) {
		super(time, priority);
		this.play = play;
	}

}
