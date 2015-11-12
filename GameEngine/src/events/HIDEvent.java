package events;

public class HIDEvent extends Event {

	private static final long serialVersionUID = -8077896804932086844L;
	public char character;
	public boolean pressed;
	public int playerID;
	
	public HIDEvent(int time, int priority, char c, boolean pressed, int playerID) {
		super(time, priority);
		character = c;
		this.pressed = pressed;
		this.playerID = playerID;
	}
}
