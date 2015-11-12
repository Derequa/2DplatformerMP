package events;

public class SpawnEvent extends Event {

	private static final long serialVersionUID = -2079299011385081720L;
	public int guid;
	
	public SpawnEvent(int time, int priority, int guid) {
		super(time, priority);
		this.guid = guid;
	}

}
