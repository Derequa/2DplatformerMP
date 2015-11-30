package events;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.PriorityQueue;

import javax.script.ScriptException;

import client.Client;
import model.*;
import scripts.ScriptManager;
import server.GameManager;

/**
 * This class implements a manager for a system of events.
 * It afford registration of specific game engine systems as listeners
 * for specific events.
 * It manages handling by priority on a given time-step.
 * @author Derek Batts
 *
 */
public class EventManager {
	
	// Priority constants
	public static final int HIGHEST = 0;
	public static final int HIGH = 1;
	public static final int LOW = 2;
	public static final int LOWEST = 3;
	
	// Make an array of queues for managing priority
	@SuppressWarnings("unchecked")
	PriorityQueue<Event>[] eventQueues = new PriorityQueue[4];
	
	// Lists of listeners
	LinkedList<Collider> colliderListeners = new LinkedList<Collider>();
	LinkedList<Client> replayers = new LinkedList<Client>();
	LinkedList<Mover> movementListeners = new LinkedList<Mover>();
	LinkedList<Spawn> spawnListeners = new LinkedList<Spawn>();
	LinkedList<Spawn> deathListeners = new LinkedList<Spawn>();
	LinkedList<HumanIO> inputListeners = new LinkedList<HumanIO>();
	LinkedList<String> listeningScripts = new LinkedList<String>();
	LinkedList<GameManager> newPlayerHandlers = new LinkedList<GameManager>();
	LinkedList<GameManager> playerQuitHandlers = new LinkedList<GameManager>();
	
	
	/**
	 * This constructs an event manager and builds its queues
	 */
	public EventManager(){
		for(int i = 0 ; i < eventQueues.length ; i++)
			eventQueues[i] = new PriorityQueue<Event>();
	}
	
	/**
	 * This determines the number of events waiting to be handled.
	 * @return The number of events waiting to be handled.
	 */
	public int numEvents(){
		int size = 0;
		for(int i = 0 ; i < 4 ; i++)
			size += eventQueues[i].size();
		return size;
	}
	
	// Event registration
	
	public void registerCollisionEvents(Collider c){
		colliderListeners.add(c);
	}
	
	public void registerReplayEvents(Client client){
		replayers.add(client);
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
	
	public void registerScriptEvent(String s){
		listeningScripts.add(s);
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
	
	public void raiseScriptEvent(ScriptEvent e){
		addEvent(e);
	}
	
	public void raiseScriptEvent(int time, int priority, int guid){
		addEvent(new ScriptEvent(time, priority, guid));
	}
	
	public void addAllEvents(Collection<Event> c){
		for(Event e : c)
			addEvent(e);
		
	}
	
	/**
	 * This method adds a generic event to its appropriate queue.
	 * @param e The event to add.
	 */
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
	
	/**
	 * This handles all the current events.
	 */
	public void handleAllEvents(){
		handleEventsAtOrBefore(Integer.MAX_VALUE);
	}
	
	/**
	 * This handles all the events at or before the given time.
	 * @param time The time to handle events at/before.
	 */
	public void handleEventsAtOrBefore(int time){
		// Loop through each queue, highest priority first
		for(int i = 0 ; i < eventQueues.length ; i++){
			// Loop through the queue handling each event and removing it
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
				else if(e instanceof PlayPauseReplayEvent){
					// Handle replay restart
					for(Client c : replayers)
						c.handlePlayPauseReplayEvent((PlayPauseReplayEvent) e);
				}
				else if(e instanceof RestartReplayEvent){
					// Handle replay restart
					for(Client c : replayers)
						c.handleReplayRestartEvent((RestartReplayEvent) e);
				}
				else if(e instanceof ReplaySpeedChangeEvent){
					// Handle replay speed change
					for(Client c : replayers)
						c.handleReplaySpeedChangeEvent((ReplaySpeedChangeEvent) e);
				}
				else if(e instanceof StopReplayEvent){
					// Handle replay recording stop
					for(Client c : replayers)
						c.handleStopReplayEvent((StopReplayEvent) e);
				}
				else if(e instanceof ReplayEvent){
					// Handle replay recording start
					for(Client c : replayers)
						c.handleReplayEvent((ReplayEvent) e);
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
				else if(e instanceof ScriptEvent){
					// Handle player quiting
					for(String s : listeningScripts){
						try {
							ScriptManager.loadScript(new FileReader(s));
							ScriptManager.bindArgument("guid", ((ScriptEvent) e).guid);
							ScriptManager.executeScript();
						} catch (FileNotFoundException | ScriptException e1) {
							e1.printStackTrace();
						}
					}
				}
			}
		}
	}
	
}
