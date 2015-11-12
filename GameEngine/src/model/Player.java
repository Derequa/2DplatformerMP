package model;

import java.awt.Rectangle;
import java.util.Hashtable;

import processing.core.PApplet;

/**
 * The Player class is a specialized version of the Box class, with
 * added functionality for player control
 * @author Derek Batts
 *
 */
public class Player extends Box {
	
	// IMPORTANT
	private static final long serialVersionUID = -8318346628289017780L;
	// A table for all my key-presses
	private Hashtable<Character, Boolean> keys = new Hashtable<Character, Boolean>();
	
	// The Player constructor requires thought be given to a spawner, movement handler, collider etc and a player number must be given
	public Player(int guid, Rectangle s, Spawn spawner, Spawn deSpawner, Mover movementHandler, Collider c, PApplet parent){
		super(guid, s, spawner, deSpawner, movementHandler, c, parent);
		// A default character values into table (set to false)
		for(char ch = 0 ; ch < 255 ; ch++)
			keys.put(new Character(ch), new Boolean(false));
	}
	
	// Getter methods
	
	public Hashtable<Character, Boolean> getKeySet(){
		return keys;
	}
	
	public boolean keyIsPressed(char c){
		return keys.get(new Character(c)).booleanValue();
	}
	
	@Override
	public boolean caresAboutFloors(){
		return true;
	}
	
	// Setter methods
	
	public void setKeyPress(char c){
		keys.put(new Character(c), new Boolean(true));
	}
	
	public void setKeyRelease(char c){
		keys.put(new Character(c), new Boolean(false));
	}
	
	
	
}
