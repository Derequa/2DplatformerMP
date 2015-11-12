package events;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

import model.*;
import server.GameManager;

public class EventManager {
	
	public static final int HIGHEST = 0;
	public static final int HIGH = 1;
	public static final int LOW = 2;
	public static final int LOWEST = 3;
	
	@SuppressWarnings("unchecked")
	PriorityQueue<Event>[] eventQueues = new PriorityQueue[4];
	
	LinkedList<Collider> colliderListeners = new LinkedList<Collider>();
	//list of replay listeners
	LinkedList<Mover> movementListeners = new LinkedList<Mover>();
	LinkedList<Spawn> spawnListeners = new LinkedList<Spawn>();
	LinkedList<Spawn> deathListeners = new LinkedList<Spawn>();
	LinkedList<HumanIO> inputListeners = new LinkedList<HumanIO>();
	LinkedList<GameManager> newPlayerHandlers = new LinkedList<GameManager>();
	LinkedList<GameManager> playerQuitHandlers = new LinkedList<GameManager>();
	
	public EventManager(){
		for(int i = 0 ; i < eventQueues.length ; i++)
			eventQueues[i] = new PriorityQueue<Event>();
	}
	
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
	
	public void registerNewPlayerEvent(GameManager g){
		newPlayerHandlers.add(g);
	}
	
	public void registerPlayerQuitEvent(GameManager g){
		playerQuitHandlers.add(g);
	}
	
	// Event raisers
	
	public void raiseCollisionEvent(CollisionEvent e){
		addEvent(e);
	}
	
	public void raiseReplayEvent(ReplayEvent e){
		addEvent(e);
	}
	
	public void raiseHIDEvent(HIDEvent e){
		addEvent(e);
	}
	
	public void raiseMovementEvent(MovementEvent e){
		addEvent(e);
	}
	
	public void raiseSpawnEvent(SpawnEvent e){
		addEvent(e);
	}
	
	public void raiseDeathEvent(DeathEvent e){
		addEvent(e);
	}
	
	public void raiseNewPlayerEvent(NewPlayerEvent e){
		addEvent(e);
	}
	
	public void raisePlayerQuitEvent(PlayerQuitEvent e){
		addEvent(e);
	}
	
	public void addAllEvents(Collection<Event> c){
		for(Event e : c)
			addEvent(e);
		
	}
	
	private void addEvent(Event e){
		if(e.priority <= HIGHEST)
			eventQueues[HIGHEST].add(e);
		else if(e.priority == HIGH)
			eventQueues[HIGH].add(e);
		else if(e.priority == LOW)
			eventQueues[LOW].add(e);
		else if(e.priority >= LOWEST)
			eventQueues[LOWEST].add(e);
	}
	
	// Event handling
	
	public void handleAllEvents(){
		handleEventsAtOrBefore(Integer.MAX_VALUE);
	}
	
	public void handleEventsAtOrBefore(int time){
		for(int i = 0 ; i < eventQueues.length ; i++){
			for(Iterator<Event> iterator = eventQueues[i].iterator(); iterator.hasNext() ; iterator.remove()){
				Event e = iterator.next();
				if(e.timestamp.compareTo(new Integer(time)) > 0)
					continue;
				if(e instanceof CollisionEvent){
					//Handle collisions
					for(Collider c : colliderListeners)
						c.handleCollisionEvent((CollisionEvent) e);
				}
				else if(e instanceof MovementEvent){
					//Handle Movement
					for(Mover m : movementListeners)
						m.handleMovementEvent((MovementEvent) e);
				}
				else if(e instanceof DeathEvent){
					// Handle spawning
					for(Spawn s : deathListeners)
						s.handleSpawnEvent((DeathEvent) e);
				}
				else if(e instanceof SpawnEvent){
					// Handle spawning
					for(Spawn s : spawnListeners)
						s.handleSpawnEvent((SpawnEvent) e);
				}
				else if(e instanceof HIDEvent){
					// Handle input
					for(HumanIO h : inputListeners)
						h.handleHIDEvent((HIDEvent) e);
				}
				else if(e instanceof ReplayEvent){
					// VAUGE STUFFS
				}
				else if(e instanceof NewPlayerEvent){
					// Handle new player
					for(GameManager g : newPlayerHandlers)
						g.handleNewPlayer((NewPlayerEvent) e);
				}
				else if(e instanceof PlayerQuitEvent){
					// Handle player quiting
					for(GameManager g : playerQuitHandlers)
						g.handlePlayerQuit((PlayerQuitEvent) e);
				}
			}
		}
	}
	
}
