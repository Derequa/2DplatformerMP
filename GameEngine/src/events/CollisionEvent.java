package events;

public class CollisionEvent extends Event {
	
	public int guid1;
	public int guid2;

	public CollisionEvent(int time, int priority, int guid1, int guid2) {
		super(time, priority);
		this.guid1 = guid1;
		this.guid2 = guid2;
	}

}
