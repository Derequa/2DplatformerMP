package events;

public class DeathEvent extends SpawnEvent{
	
	private static final long serialVersionUID = -396472475433684516L;

	public DeathEvent(int time, int priority, int guid) {
		super(time, priority, guid);
	}

}
