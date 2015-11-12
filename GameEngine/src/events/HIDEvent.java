package events;

/**
 * This class models a simple event for player input.
 * @author Derek Batts
 *
 */
public class HIDEvent extends Event {

	private static final long serialVersionUID = -8077896804932086844L;
	// What key was pressed/released
	public char character;
	// Was the key pressed or released
	public boolean pressed;
	// Who pressed/released the key
	public int playerID;
	
	/**
	 * This makes an HIDEvent.
	 * @param time The time to mark this event with.
	 * @param priority The priority to give this event.
	 * @param c The character that was pressed/released.
	 * @param pressed The boolean determining if the key was pressed/released.
	 * @param playerID The ID of the player the input is coming from.
	 */
	public HIDEvent(int time, int priority, char c, boolean pressed, int playerID) {
		super(time, priority);
		character = c;
		this.pressed = pressed;
		this.playerID = playerID;
	}
}
