package model;

import java.awt.Rectangle;

import processing.core.PApplet;

/**
 * This class models a game object who acts like a static object / platform.
 * but can move within a certain bounds.
 * @author Derek Batts
 *
 */
public class MovingPlatform extends GameObject {
	
	private static final long serialVersionUID = 8951325835911774853L;
	// Fields for bounds and velocity
	public int vMagX = 0;
	public int vMagY = 0;
	public int xMax = 0;
	public int xMin = 0;
	public int yMax = 0;
	public int yMin = 0;
	// A Spawn system if we are associated with one
	public Spawn s = null;

	/**
	 * This constructs a blank MovingPlatform with the given systems.
	 * @param guid Our ID.
	 * @param s Our shape.
	 * @param parent Our owner.
	 * @param m Our Movement system.
	 * @param specialSpawn The spawn we are linked to.
	 */
	public MovingPlatform(int guid, Rectangle s, PApplet parent, Mover m, Spawn specialSpawn) {
		super(guid, s, null, null, m, null, parent);
		this.s = specialSpawn;
	}

	/**
	 * This will always be treated like a static object.
	 */
	@Override
	public boolean isStatic() {
		return true;
	}
	
	/**
	 * Set our position (X) and update our spawn system if present.
	 */
	@Override
	public void posSetX(int x){
		super.posSetX(x);
		if(s != null)
			s.moveSpawn(x, s.getY());
	}
	
	/**
	 * Set our position (Y) and update our spawn system if present.
	 */
	@Override
	public void posSetY(int y){
		super.posSetY(y);
		if(s != null)
			s.moveSpawn(s.getX(), y);
	}
	
	/**
	 * Set our position and update our spawn system if present.
	 */
	@Override
	public void posSet(int x, int y){
		super.posSet(x, y);
		if(s != null)
			s.moveSpawn(x, y);
	}

}
