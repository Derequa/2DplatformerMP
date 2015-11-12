package model;

import java.io.Serializable;
import java.util.Hashtable;

import events.MovementEvent;

/**
 * This class handles movement update for objects that implement the Moveable interface
 * @author Derek Batts
 *
 */
public class Mover implements Serializable {
	
	private static final long serialVersionUID = 7973530547190934079L;
	// A table of all the object in the game
	Hashtable<Integer, GameObject> objects = null;
	
	/**
	 * This makes a new Mover system and links it to all the objects in the game.
	 * @param objects The master list of objects in the game.
	 */
	public Mover(Hashtable<Integer, GameObject> objects){
		this.objects = objects;
	}
	
	/**
	 * This method updates a Moveable object according to its state.
	 * @param m The object to move / update.
	 */
	public void update(Moveable m){
		// Update position based on velocity
		m.posSet((int) (m.posGetX() + m.vGetX()), (int) (m.posGetY() + m.vGetY()));
		// Update velocity based on acceleration
		m.vSetX(m.vGetX() + m.aGetX());
		if(!m.isOnFloor())
			m.vSetY(m.vGetY() + m.aGetY());
		else m.vSetY(0.0f);
		
		// Limit velocity
		if(m.vGetX() > Moveable.V_MAX)
			m.vSetX(Moveable.V_MAX);
		else if(m.vGetX() < -Moveable.V_MAX)
			m.vSetX(-Moveable.V_MAX);
		if(m.vGetY() > Moveable.V_MAX)
			m.vSetY(Moveable.V_MAX);
		else if(m.vGetY() < -Moveable.V_MAX)
			m.vSetY(-Moveable.V_MAX);
		
		// Check floor friction
		if(m.isOnFloor() && m.caresAboutFloors()){
			if(m.vGetX() > 0.0f){
				m.vSetX(m.vGetX() - Moveable.FLOOR_FRICTION);
				if(m.vGetX() < 0.0f)
					m.vSetX(0.0f);
			}
			else if(m.vGetX() < 0.0f){
				m.vSetX(m.vGetX() + Moveable.FLOOR_FRICTION);
				if(m.vGetX() > 0.0f)
					m.vSetX(0.0f);
			}
		}
	}
	
	/**
	 * This method update a MovingPlatform according to its state.
	 * @param p
	 */
	public void update(MovingPlatform p){
		// Calculate new position
		int newX = p.posGetX() + p.vMagX;
		int newY = p.posGetY() + p.vMagY;
		// Check if we move past our bounds and change direction if we do
		if((newX > p.xMax) || (newX < p.xMin))
			p.vMagX *= -1;
		else
			p.posSetX(newX);
		if((newY > p.yMax) || (newY < p.yMin))
			p.vMagY *= -1;
		else
			p.posSetY(newY);
		
	}
	
	/**
	 * This method handles teleporting a GameObject to arbitrary coordinates.
	 * @param e The event describing the movement.
	 */
	public void handleMovementEvent(MovementEvent e){
		if(objects.containsKey(new Integer(e.guid))){
			GameObject g = objects.get(new Integer(e.guid));
			g.posSet(e.x, e.y);
		}
	}
}
