package model;

import java.util.Hashtable;

import events.MovementEvent;

/**
 * This class handles movement update for objects that implement the moveable interface
 * @author Derek
 *
 */
public class Mover {
	
	Hashtable<Integer, GameObject> objects = null;
	
	public Mover(Hashtable<Integer, GameObject> objects){
		this.objects = objects;
	}
	
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
	
	public void update(MovingPlatform p){
		int newX = p.posGetX() + p.vMagX;
		int newY = p.posGetY() + p.vMagY;
		if((newX > p.xMax) || (newX < p.xMin))
			p.vMagX *= -1;
		else
			p.posSetX(newX);
		if((newY > p.yMax) || (newY < p.yMin))
			p.vMagY *= -1;
		else
			p.posSetY(newY);
		
	}
	
	public void handleMovementEvent(MovementEvent e){
		if(objects.containsKey(new Integer(e.guid))){
			GameObject g = objects.get(new Integer(e.guid));
			g.posSet(e.x, e.y);
		}
	}
}
