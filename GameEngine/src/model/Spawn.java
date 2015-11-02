package model;

import java.awt.Rectangle;

/**
 * This class wraps up a few simple data fields and methods
 * retaled to a spawn (or de-spawn)
 * @author Derek Batts
 *
 */
public class Spawn {
	
	// The rectangle defining the boundary
	private Rectangle boundary;
	// Is the spawn active
	boolean isActive;
	
	/**
	 * A spawn is constructed around a rectangle and is set active by default
	 * @param bounds
	 */
	public Spawn(Rectangle bounds){
		this.boundary = bounds;
		isActive = true;
	}
	
	// Getter methods
	
	public int getX(){
		return boundary.x;
	}
	
	public int getY(){
		return boundary.y;
	}
	
	public Rectangle getBounds(){
		return boundary;
	}
	
	public boolean isActive(){
		return this.isActive;
	}
	
	// Setter methods
	
	public void setActive(boolean bool){
		this.isActive = bool;
	}
	
	public void moveSpawn(int x, int y){
		boundary.x = x;
		boundary.y = y;
	}
	
	public void setBounds(Rectangle bounds){
		this.boundary = bounds;
	}
}
