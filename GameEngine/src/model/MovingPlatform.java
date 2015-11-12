package model;

import java.awt.Rectangle;

import processing.core.PApplet;

public class MovingPlatform extends GameObject {
	
	private static final long serialVersionUID = 8951325835911774853L;
	public int vMagX = 0;
	public int vMagY = 0;
	public int xMax = 0;
	public int xMin = 0;
	public int yMax = 0;
	public int yMin = 0;
	public Spawn s = null;

	public MovingPlatform(int guid, Rectangle s, PApplet parent, Mover m, Spawn specialSpawn) {
		super(guid, s, null, null, m, null, parent);
		this.s = specialSpawn;
	}

	@Override
	public boolean isStatic() {
		return true;
	}
	
	@Override
	public void posSetX(int x){
		super.posSetX(x);
		if(s != null)
			s.moveSpawn(x, s.getY());
	}
	
	@Override
	public void posSetY(int y){
		super.posSetY(y);
		if(s != null)
			s.moveSpawn(s.getX(), y);
	}
	
	@Override
	public void posSet(int x, int y){
		super.posSet(x, y);
		if(s != null)
			s.moveSpawn(x, y);
	}

}
