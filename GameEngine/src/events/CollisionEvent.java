package events;

public class CollisionEvent extends Event {
	
	private static final long serialVersionUID = -8289416432572582366L;
	public int guid1;
	public int guid2;

	public CollisionEvent(int time, int priority, int guid1, int guid2) {
		super(time, priority);
		this.guid1 = guid1;
		this.guid2 = guid2;
	}

}
