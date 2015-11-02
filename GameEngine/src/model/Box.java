package model;

import java.awt.Rectangle;

import processing.core.PApplet;

/**
 * The Box class models a movable version of a rectangular game object
 * @author Derek Batts
 *
 */
public class Box extends GameObject implements Moveable{
	
	// Important
	private static final long serialVersionUID = -6195246797653458854L;
	
	// Velocity fields
	private float vX = 0.0f;
	private float vY = 0.0f;
	// Acceleration fields
	private float aX = 0.0f;
	private float aY = 0.0f;
	
	// Collision fields
	private boolean onFloor = false;
	private boolean collided = false;
	private boolean doICare = false;

	// Create a plain box with no spawn, death-zone, mover, or collier
	public Box(Rectangle s, PApplet parent) {
		super(s, parent);
		super.spawner = null;
		super.deSpawner = null;
	}
	
	// Create a Box with the given behavior objects
	public Box(Rectangle s, Spawn spawner, Spawn deSpawner, Mover movementHandler, Collider c, PApplet parent){
		super(s, spawner, deSpawner, movementHandler, c, parent);
	}
	
	//Getters

	public float vGetX() {
		return vX;
	}

	public float vGetY() {
		return vY;
	}
	
	public float aGetX() {
		return aX;
	}

	public float aGetY() {
		return aY;
	}
	
	/**
	 * For remembering if we are on a floor
	 */
	public boolean isOnFloor() {
		return onFloor;
	}
	
	/**
	 * For remembering if we are collided
	 */
	public boolean isCollided(){
		return collided;
	}
	
	/**
	 * For remembering if I slow down on floors
	 */
	public boolean caresAboutFloors() {
		return doICare;
	}

	//Setters
	
	public void vSetX(float x) {
		this.vX = x;
	}

	public void vSetY(float y) {
		this.vY = y;
	}

	public void vSet(float x, float y) {
		this.vX = x;
		this.vY = y;
	}

	public void aSetX(float x) {
		this.aX = x;
	}

	public void aSetY(float y) {
		this.aY = y;
	}

	public void aSet(float x, float y) {
		this.aX = x;
		this.aY = y;
	}
	
	public void setOnFloor(boolean b) {
		onFloor = b;
	}
	
	public void setCollided(boolean b){
		collided = b;
	}

	public void setCaresAboutFloors(boolean b) {
		doICare = b;
	}

	@Override
	public boolean isStatic() {
		return false;
	}

}
