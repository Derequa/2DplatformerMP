package model;

import java.io.Serializable;
import java.util.Hashtable;

import events.HIDEvent;

/**
 * This class handles responding to player input events.
 * @author Derek Batts
 *
 */
public class HumanIO implements Serializable{

	public static final long serialVersionUID = -2016115109726917061L;
	// The link to the master table of game objects
	private Hashtable<Integer, GameObject> objects;
	
	/**
	 * This constructs a HumanIO system and connects it to the list of objects.
	 * @param objects The master list of objects in the game.
	 */
	public HumanIO(Hashtable<Integer, GameObject> objects){
		this.objects = objects;
	}

	/**
	 * This method handles an event about new input from the player.
	 * @param e The event describing player input.
	 */
	public void handleHIDEvent(HIDEvent e){
		// Check the GUID in our table and see if it is a player
		if(objects.containsKey(new Integer(e.playerID)) && (objects.get(new Integer(e.playerID)) instanceof Player)){
			// Set the key pressed or released for the given player
			if(e.pressed)
				((Player) objects.get(new Integer(e.playerID))).setKeyPress(e.character);
			else
				((Player) objects.get(new Integer(e.playerID))).setKeyRelease(e.character);
		}
	}
	
	/**
	 * This method handles input for a given player
	 * @param p The player to handle input for
	 */
	public void handleInput(Player p){
		// Check for jump key press and make sure we aren't hitting a ceiling
		if((p.keyIsPressed('w') || p.keyIsPressed('W')) && !(p.isCollided() && !p.isOnFloor()))
			p.vSetY(-3.0f);
		
		// Booleans for left and right key press
		boolean left = (p.keyIsPressed('a') || p.keyIsPressed('A'));
		boolean right = (p.keyIsPressed('d') || p.keyIsPressed('D'));
		
		// Only move left/right if its the only one pressed and we aren't hitting a wall
		if(left && !right && !(p.isCollided() && !p.isOnFloor()) && !p.collidedFromRight)
			p.vSetX(-3.0f);
		else if(right && !left && !(p.isCollided() && !p.isOnFloor()) && !p.collidedFromLeft)
			p.vSetX(3.0f);
	}
}
