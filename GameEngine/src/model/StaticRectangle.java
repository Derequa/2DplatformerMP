package model;

import java.awt.Rectangle;

import processing.core.PApplet;

/**
 * This class is a simple extensions and usable version of game object
 * that only overrides the isSatatic method. This is meant to implement
 * static platforms.
 * @author Derek Batts
 *
 */
public class StaticRectangle extends GameObject {

	// Wooo
	private static final long serialVersionUID = -6776604854107270744L;

	// Create a simple Rectangle based game object
	public StaticRectangle(int guid, Rectangle s, PApplet parent) {
		super(guid, s, parent);
	}
	
	@Override
	/**
	 * This type of game object will always be defined as static
	 */
	public boolean isStatic(){
		return true;
	}

}
