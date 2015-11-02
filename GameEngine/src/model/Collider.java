package model;

/**
 * This class defines and handles collisions between rectangle GameObjects
 * @author Derek Batts
 *
 */
public class Collider {
	
	/**
	 * This is a front-end method that collides any two rectangle game objects
	 * @param g1
	 * @param g2
	 */
	public void collide(GameObject g1, GameObject g2){
		// Are they both moveable
		if (g1.isMoveable() && g2.isMoveable() && !(g1 instanceof Player) && !(g2 instanceof Player))
			collide2Moveable((Moveable) g1, (Moveable) g2);
		// Is at least one moveable
		else if (g1.isMoveable())
			collideMoveableStatic((Moveable) g1, g2);
		else if (g2.isMoveable())
			collideMoveableStatic((Moveable) g2, g1);
		// Two static objects should never collide
		else throw new IllegalArgumentException("Static Objects Shouldn't be colliding!");
	}
	
	// An easy way to check collisions
	public boolean collides(GameObject g1, GameObject g2){
		return g1.getShape().intersects(g2.getShape());
	}
	
	// A helper method for colliding two moveable objects
	private void collide2Moveable(Moveable m1, Moveable m2){
		if(m1.getShape().intersects(m2.getShape().getBounds2D())){
			if(m1.isCollided()) return;
			float m1VX = m1.vGetX();
			float m1VY = m1.vGetY();
			
			// We dont swap a velocity vector if our centers are equal
			if(m1VX != 0){
				m1.vSetX(m2.vGetX());
				m2.vSetX(m1VX);
			}
			if(m1VY != 0){
				m1.vSetY(m2.vGetY());
				m2.vSetY(m1VY);
			}
			
			m1.setCollided(true);
		} else m1.setCollided(false);;
	}
	
	// A helper method for colliding between a moveable object and a static object
	private void collideMoveableStatic(Moveable m, GameObject g){
		if(m.getShape().intersects(g.getShape().getBounds2D())){
			
			// Get previous bounds
			int myOldLeft = ((GameObject) m).oldX;
			int myOldRight = myOldLeft + m.getShape().width;
			int myOldTop = ((GameObject) m).oldY;
			int myOldBottom = myOldTop + m.getShape().height;
			
			// Get current bounds
			int myLeft = m.posGetX();
			int myRight = myLeft + m.getShape().width;
			int myTop = m.posGetY();
			int myBottom = myTop + m.getShape().height;
			
			// Get other objet's bounds
			int otherLeft = g.posGetX();
			int otherRight = otherLeft + g.getShape().width;
			int otherTop = g.posGetY();
			int otherBottom = otherTop + g.getShape().height;
			
			// Determine where the collision came from
			boolean collidedFromLeft = (myOldRight <= otherLeft) && (myRight >= otherLeft);
			boolean collidedFromRight = (myOldLeft >= otherRight) && (myLeft <= otherRight);
			boolean collidedFromTop = (myOldBottom <= otherTop) && (myBottom >= otherTop);
			boolean collidedFromBottom = (myOldTop >= otherBottom) && (myTop <= otherBottom);
			
			// Respond to colliding from top
			if(collidedFromTop){
				((GameObject) m).collidedFromTop = true;
				((GameObject) m).colliderTop = g.getShape();
				// Only change m if it is not collided
				if(!m.isCollided()){
					if(m.caresAboutFloors() && g.isStatic()){
						m.setOnFloor(true);
						m.vSetY(0.0f);
					} else m.vSetY(-m.vGetY());
				}
			}
			// Respond to colliding from bottom
			else if(collidedFromBottom){
				((GameObject) m).collidedFromBottom = true;
				((GameObject) m).colliderBottom = g.getShape();
				if(!m.isCollided())
					m.vSetY(-m.vGetY());
			}
			// Respond to colliding from side
			if(collidedFromLeft || collidedFromRight){
				if(!m.isCollided())
					m.vSetX(-m.vGetX());
				if(collidedFromLeft){
					((GameObject) m).collidedFromLeft = true;
					((GameObject) m).colliderLeft = g.getShape();
				}
				if(collidedFromRight){
					((GameObject) m).collidedFromRight = true;
					((GameObject) m).colliderRight = g.getShape();
				}
			}
			
			// Signal collision
			m.setCollided(true);
			((GameObject) m).collidedWith.put(g.getShape(), new Boolean(true));
		} 
		else {
			// Signal we are no longer collided
			((GameObject) m).collidedWith.put(g.getShape(), new Boolean(false));
			if(((GameObject) m).colliderLeft != null && ((GameObject) m).colliderLeft.equals(g.getShape()))
				((GameObject) m).collidedFromLeft = false;
			else if(((GameObject) m).colliderRight != null && ((GameObject) m).colliderRight.equals(g.getShape()))
				((GameObject) m).collidedFromRight = false;
			else if(((GameObject) m).colliderTop != null && ((GameObject) m).colliderTop.equals(g.getShape()))
				((GameObject) m).collidedFromTop = false;
			else if(((GameObject) m).colliderBottom != null && ((GameObject) m).colliderBottom.equals(g.getShape()))
				((GameObject) m).collidedFromBottom = false;
		}
		// Check for no collisions
		if(!((GameObject) m).collidedWith.containsValue(new Boolean(true))){
			m.setCollided(false);
			m.setOnFloor(false);
			((GameObject) m).collidedFromBottom = false;
			((GameObject) m).collidedFromLeft = false;
			((GameObject) m).collidedFromRight = false;
			((GameObject) m).collidedFromTop = false;
		}
	}
}
