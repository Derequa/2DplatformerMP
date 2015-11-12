package events;

public class MovementEvent extends Event {

	private static final long serialVersionUID = 1427383018918831936L;
	public int guid;
	public int x;
	public int y;
	
	public MovementEvent(int time, int priority, int guid, int x, int y) {
		super(time, priority);
		this.guid = guid;
		this.x = x;
		this.y = y;
	}

}
