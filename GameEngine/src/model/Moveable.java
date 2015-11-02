package model;

import java.awt.Rectangle;

/** An interface that performs movement updates based around the objects center */
public interface Moveable {
	
	public static final float V_MAX = 6.5f;
	public static final float FLOOR_FRICTION = 0.25f;
	public static final float GRAVITY = 0.9f;
	
	public float vGetX();
	public float vGetY();
	public float aGetX();
	public float aGetY();
	public int posGetX();
	public int posGetY();
	public boolean isOnFloor();
	public boolean isCollided();
	public boolean caresAboutFloors();
	
	public Rectangle getShape();
	
	public void vSetX(float x);
	public void vSetY(float y);
	public void vSet(float x, float y);
	public void aSetX(float x);
	public void aSetY(float y);
	public void aSet(float x, float y);
	public void posSetX(int x);
	public void posSetY(int y);
	public void posSet(int x, int y);
	public void setOnFloor(boolean b);
	public void setCollided(boolean b);
	public void setCaresAboutFloors(boolean b);
}
