package events;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

import model.*;

public class EventManager {
	Queue<Event> currentEvents = new PriorityQueue<Event>();	
	
	LinkedList<Collider> colliderListeners = new LinkedList<Collider>();
	//list of replay listeners
	LinkedList<Mover> movementListeners = new LinkedList<Mover>();
	LinkedList<Spawn> spawnListeners = new LinkedList<Spawn>();
	LinkedList<Spawn> deathListeners = new LinkedList<Spawn>();
	LinkedList<HumanIO> inputListeners = new LinkedList<HumanIO>();
	
	// Event registration
	
	public void registerCollisionEvents(Collider c){
		colliderListeners.add(c);
	}
	
	public void registerReplayEvents(/*REPLAY SYSTEM*/){
		
	}
	
	public void registerMovementEvent(Mover m){
		movementListeners.add(m);
	}
	
	public void registerSpawnEvent(Spawn s){
		spawnListeners.add(s);
	}
	
	public void registerDeathEvent(Spawn s){
		deathListeners.add(s);
	}
	
	public void registerHIDEvent(HumanIO h){
		inputListeners.add(h);
	}
	
	// Event raisers
	
	public void raiseCollisionEvent(CollisionEvent e){
		currentEvents.add(e);
	}
	
	public void raiseReplayEvent(ReplayEvent e){
		currentEvents.add(e);
	}
	
	public void raiseHIDEvent(HIDEvent e){
		currentEvents.add(e);
	}
	
	public void raiseMovementEvent(MovementEvent e){
		currentEvents.add(e);
	}
	
	public void raiseSpawnEvent(SpawnEvent e){
		currentEvents.add(e);
	}
	
	public void raiseDeathEvent(DeathEvent e){
		currentEvents.add(e);
	}
	
	// Event handling
	
	public void handleEvents(){
		
		for(Iterator<Event> iterator = currentEvents.iterator(); iterator.hasNext() ; iterator.remove()){
			Event e = iterator.next();
			if(e instanceof CollisionEvent){
				for(Collider c : colliderListeners){
					//Handle collisions
					c.handleCollisionEvent((CollisionEvent) e);
				}
			}
			else if(e instanceof MovementEvent){
				for(Mover m : movementListeners){
					//Handle Movement
					m.handleMovementEvent((MovementEvent) e);
				}
			}
			else if(e instanceof DeathEvent){
				for(Spawn s : deathListeners){
					// Handle spawning
					s.handleSpawnEvent((DeathEvent) e);
				}
			}
			else if(e instanceof SpawnEvent){
				for(Spawn s : spawnListeners){
					// Handle spawning
					s.handleSpawnEvent((SpawnEvent) e);
				}
			}
			else if(e instanceof HIDEvent){
				for(HumanIO h : inputListeners){
					h.handleHIDEvent((HIDEvent) e);
				}
			}
			else if(e instanceof ReplayEvent){
				// VAUGE STUFFS
			}
		}
	}
	
}
