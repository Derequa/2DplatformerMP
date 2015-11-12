package model;

import java.awt.Rectangle;
import java.io.Serializable;
import java.util.Hashtable;

import events.DeathEvent;
import events.SpawnEvent;

/**
 * This class wraps up a few simple data fields and methods
 * retaled to a spawn (or de-spawn)
 * @author Derek Batts
 *
 */
public class Spawn implements Serializable{
	
	private static final long serialVersionUID = 2479661271009259133L;

	Hashtable<Integer, GameObject> objects;
	
	// The rectangle defining the boundary
	private Rectangle boundary;
	// Is the spawn active
	boolean isActive;
	
	/**
	 * A spawn is constructed around a rectangle and is set active by default
	 * @param bounds
	 */
	public Spawn(Rectangle bounds, Hashtable<Integer, GameObject> objects){
		this.boundary = bounds;
		isActive = true;
		this.objects = objects;
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
	
	public void handleSpawnEvent(SpawnEvent e){
		if(!objects.containsKey(new Integer(e.guid)))
			return;
		if(e instanceof DeathEvent)
			objects.get(new Integer(e.guid)).setVisible(false);
		objects.get(new Integer(e.guid)).spawn();
	}
}
