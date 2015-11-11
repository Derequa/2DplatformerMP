package model;

import java.awt.Rectangle;
import java.io.Serializable;
import java.util.Hashtable;

import processing.core.*;

/**
 * This class defines the base fields and methods for a generic game object
 * in our engine. It attempts to be a generic component model, where certain actions
 * and components are changeable at runtime.
 * @author Derek
 *
 */
public abstract class GameObject implements Serializable{

	// An important thingy
	private static final long serialVersionUID = 1916370976096731770L;
	private Integer guid;
	// The Java.awt shape associated with this game object
	private Rectangle javaShape = null;
	// Where do I spawnn
	protected Spawn spawner = null;
	// Where do I get ded
	protected Spawn deSpawner = null;
	// How do I move
	protected Mover movement = null;
	// What happens when I hit stuff
	protected Collider collisionDetecter;
	// Who my daddy
	protected PApplet parent;
	// Am I visible
	private boolean visible = false;
	// An array of ints for RGB color
	private int[] color = {0, 0, 0};
	// Old X and Y coordinates
	public int oldX;
	public int oldY;
	// A table of I am collided with
	public Hashtable<GameObject, Boolean> collidedWith = new Hashtable<GameObject, Boolean>();
	//Booleans for what direction we collided in
	public boolean collidedFromLeft;
	public boolean collidedFromRight;
	public boolean collidedFromTop;
	public boolean collidedFromBottom;
	// Rectangle pointers to who collided with us in what direction
	public GameObject colliderLeft;
	public GameObject colliderRight;
	public GameObject colliderTop;
	public GameObject colliderBottom;
	
	/**
	 * This defines a constructor for a game object with a specific rectangle
	 * @param s The Rectangle to shape us
	 * @param parent The PApplet in control
	 */
	public GameObject(int guid, Rectangle s, PApplet parent){
		this.guid = new Integer(guid);
		this.javaShape = s;
		this.parent = parent;
		oldX = javaShape.x;
		oldY = javaShape.y;
	}
	
	/**
	 * This defines a constructor for a game object with a specific rectangle and objects dfinign action
	 * for this game object
	 * @param s The Rectangle for our shape
	 * @param spawner A place to spawn
	 * @param deSpawner A place to die
	 * @param movementHandler A movement definer
	 * @param c A collision definer
	 * @param parent The parent of this object
	 */
	public GameObject(int guid, Rectangle s, Spawn spawner, Spawn deSpawner, Mover movementHandler, Collider c, PApplet parent){
		this(guid, s, parent);
		this.spawner = spawner;
		this.deSpawner = deSpawner;
		this.movement = movementHandler;
		this.collisionDetecter = c;
	}
	
	//Getter methods
	
	public Integer getGUID(){
		return guid;
	}
	
	public int posGetX(){
		return javaShape.x;
	}
	public int posGetY(){
		return javaShape.y;
	}
	public Rectangle getShape(){
		return this.javaShape;
	}
	
	public boolean visible(){
		return this.visible;
	}
	
	public int[] getColor(){
		return color;
	}
	
	public Collider getCollider(){
		return this.collisionDetecter;
	}
	
	//Setter Methods
	
	public void posSetX(int x){
		oldX = javaShape.x;
		javaShape.x = x;
	}
	public void posSetY(int y){
		oldY = javaShape.y;
		javaShape.y = y;
	}
	public void posSet(int x, int y){
		oldX = javaShape.x;
		oldY = javaShape.y;
		javaShape.x = x;
		javaShape.y = y;
	}
	
	public void setVisible(boolean b){
		this.visible = b;
	}
	
	public void setColor(int r, int g, int b){
		if((r < 0) || (r > 255) || (g < 0) || (g > 255) || (b < 0) || (b > 255))
			throw new IllegalArgumentException("Invalid RGB color value");
		color[0] = r;
		color[1] = g;
		color[2] = b;
	}
	
	public boolean isMoveable(){
		return this.movement != null;
	}
	
	public boolean isSpawnable(){
		return this.spawner != null;
	}
	
	public boolean isKillable(){
		return this.deSpawner != null;
	}
	
	public abstract boolean isStatic();
	
	/**
	 * This method moves the game object according to its mover, if it is moveable
	 */
	public void move(){
		if(this.isMoveable()){
			if(this instanceof Moveable){
				this.movement.update((Moveable) this);
			}
			else if(this instanceof MovingPlatform){
				this.movement.update((MovingPlatform) this); 
			}
		}
	}
	
	/**
	 * A wrapper for drawing a rectangle in processing
	 */
	public void draw(){
		if(!visible) return;
		parent.fill(parent.color(color[0], color[1], color[2]));
		parent.rect((float) javaShape.x,(float) javaShape.y,(float) javaShape.width,(float) javaShape.height);
	}
	
	/**
	 * A method for moving back to spawn, if one is defined
	 */
	public void spawn(){
		if(isSpawnable() && spawner.isActive()){
			javaShape.x = this.spawner.getX();
			javaShape.y = this.spawner.getY();
			this.visible = true;
			if(isMoveable()){
				Moveable m = (Moveable) this;
				// Reset velocity for Player objects
				if(!(this instanceof Box))
					m.vSet(0.0f, 0.0f);
				m.setCollided(false);
				m.setOnFloor(false);
			}
		}
	}
	
	/**
	 * A method that determines if we are in the de-spawner
	 * @return True if we gon die
	 */
	public boolean inDeathZone(){
		if(!isKillable()) return false;
		return javaShape.intersects(deSpawner.getBounds());
	}
	
	@Override
	public boolean equals(Object o){
		if(o instanceof GameObject)
			return ((GameObject) o).getGUID().equals(getGUID());
		else return false;
	}
}
