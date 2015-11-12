package events;

public class RestartReplayEvent extends ReplayEvent {

	private static final long serialVersionUID = -1958326140375860798L;

	public RestartReplayEvent(int time, int priority) {
		super(time, priority);
	}

}
