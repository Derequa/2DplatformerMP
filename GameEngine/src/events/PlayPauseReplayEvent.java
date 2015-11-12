package events;

public class PlayPauseReplayEvent extends ReplayEvent {

	private static final long serialVersionUID = 8199417468197397810L;

	public boolean play;
	public PlayPauseReplayEvent(int time, int priority, boolean play) {
		super(time, priority);
		this.play = play;
	}

}
