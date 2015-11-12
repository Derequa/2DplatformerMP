package events;

public class ReplayEvent extends Event {

	private static final long serialVersionUID = -4886103220855434509L;

	public ReplayEvent(int time, int priority) {
		super(time, priority);
	}

}
