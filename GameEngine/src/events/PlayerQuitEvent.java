package events;

public class PlayerQuitEvent extends Event {

	private static final long serialVersionUID = 6365457343803170693L;

	public int guid;
	public PlayerQuitEvent(int time, int priority, int guid) {
		super(time, priority);
		this.guid = guid;
	}

}
