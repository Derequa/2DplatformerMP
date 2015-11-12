package events;

public class ReplaySpeedChangeEvent extends ReplayEvent {

	private static final long serialVersionUID = -1306998277039799173L;

	public int newSpeed;
	public ReplaySpeedChangeEvent(int time, int priority, int newSpeed) {
		super(time, priority);
		this.newSpeed = newSpeed;
	}

}
