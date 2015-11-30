package events;

public class ScriptEvent extends Event {

	private static final long serialVersionUID = 790238686263125928L;

	int guid;
	
	public ScriptEvent(int time, int priority, int guid) {
		super(time, priority);
		this.guid = guid;
	}

}
