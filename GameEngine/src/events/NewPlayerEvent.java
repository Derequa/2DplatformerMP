package events;

public class NewPlayerEvent extends Event {

	private static final long serialVersionUID = 570891563212325873L;

	public int guid;
	public NewPlayerEvent(int time, int priority, int guid) {
		super(time, priority);
		this.guid = guid;
	}

}
