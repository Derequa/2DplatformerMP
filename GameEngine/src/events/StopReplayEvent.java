package events;

public class StopReplayEvent extends ReplayEvent {

	private static final long serialVersionUID = -8154059349516412773L;

	public StopReplayEvent(int time, int priority) {
		super(time, priority);
	}

}
