package server;

import java.awt.Rectangle;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.Semaphore;

import events.*;
import model.*;
import processing.core.PApplet;
import processing.event.KeyEvent;
import time.TimeLine;

/**
 * This class displays and updates the game world. It also starts up server-client communication
 * and sends out world state to all clients on each step.
 * @author Derek Batts
 *
 */
public class GameManager extends PApplet {
	
	// Important thing
	private static final long serialVersionUID = 1227957981784275051L;
	// Global time-line
	public static TimeLine globalTime = new TimeLine(0, 2, Integer.MAX_VALUE);
	// A list of all the Servers handling clients
	protected static LinkedList<Server> servers = new LinkedList<Server>();
	// A list of all the clients connected
	protected static LinkedList<Socket> clients = new LinkedList<Socket>();
	// A table mapping players to the server thread/client controlling them
	protected static Hashtable<Player, Server> playerServerMap = new Hashtable<Player, Server>();
	// A thread to listen for incoming connections
	protected ConnectionManager listener = new ConnectionManager(this);
	private Thread listenerThread = new Thread(listener);
	
	// A random object for general use
	private static Random rand = new Random();
	// A semaphore to lock the list of players
	public static Semaphore stateLock = new Semaphore(1);
	
	public static EventManager eventManager = new EventManager();
	
	public static LinkedList<Event> sendableEvents = new LinkedList<Event>();
	
	public static final boolean debug = true;
	public Player p1 = null;
	
	public static Hashtable<Integer, GameObject> objects = new Hashtable<Integer, GameObject>();
	
	// A spawn for players (and boxes)
	protected static Spawn playerSpawn;
	// A death zone for players
	protected static Spawn playerDeathZone;
	protected static MovingPlatform death = null;
	// A collision detector
	protected static Collider collisionDetector = new Collider(objects);
	// A motion updater
	protected static Mover motionUpdater = new Mover(objects);
	
	protected static HumanIO hidHandler = new HumanIO(objects);
	
	// Window size stuff
	private static final int WIDTH = 500;
	private static final int HEIGHT = 500;
	
	// Object marker
	public static int guidMaker = 0;
	// A place to print (deprecated)
	@Deprecated
	protected static OutputStream console = System.out;
	// A rectangle defining the window
	StaticRectangle window;
	
	/**
	 * This method sets up the game
	 */
	public void setup(){
		// Start listening for new connections
		listenerThread.start();
		
		try {
			stateLock.acquire();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		
		// Create spawn and death zones
		Rectangle spawnBounds = new Rectangle(2, 30, 30, 30);
		Rectangle despawnBounds = new Rectangle(15, 260, 70, 20);
		playerSpawn = new Spawn(spawnBounds, objects);
		playerDeathZone = new Spawn(despawnBounds, objects);
		window = new StaticRectangle(guidMaker++, new Rectangle(0, 0, WIDTH, HEIGHT), this);
		
		eventManager.registerCollisionEvents(collisionDetector);
		eventManager.registerDeathEvent(playerDeathZone);
		eventManager.registerSpawnEvent(playerSpawn);
		eventManager.registerMovementEvent(motionUpdater);
		eventManager.registerHIDEvent(hidHandler);
		eventManager.registerNewPlayerEvent(this);
		eventManager.registerPlayerQuitEvent(this);
		
		// Create all the Boxes
		for(int i = 0 ; i < 4 ; i++){
			Rectangle box = new Rectangle((WIDTH / 2) - 30, 10, 30, 30);
			Box b = new Box(guidMaker++, box, playerSpawn, playerDeathZone, motionUpdater, collisionDetector, this);
			b.setColor(255, 255, 255);
			b.setVisible(true);
			b.vSet(rand.nextInt(4),rand.nextInt(4));
			objects.put(b.getGUID(), b);
		}
		
		// Create all the walls
		StaticRectangle[] walls = { new StaticRectangle(guidMaker++, new Rectangle(0, 0, WIDTH, 1), this),
									new StaticRectangle(guidMaker++, new Rectangle(0, HEIGHT, WIDTH, 1), this),
									new StaticRectangle(guidMaker++, new Rectangle(WIDTH, 0, 1, HEIGHT), this),
									new StaticRectangle(guidMaker++, new Rectangle(0, 0, 1, HEIGHT), this)};
		for(int i = 0 ; i < 4 ; i++)
			objects.put(walls[i].getGUID(), walls[i]);
		
		// Create two platforms  to jump on
		StaticRectangle platform1 = new StaticRectangle(guidMaker++, new Rectangle(350 - 85, 350 + 20, 85, 20), this);
		platform1.setColor(255, 255, 255);
		platform1.setVisible(true);
		objects.put(platform1.getGUID(), platform1);
		StaticRectangle platform2 = new StaticRectangle(guidMaker++, new Rectangle(10, 125 + 20, 85, 20), this);
		platform2.setColor(255, 255, 255);
		platform2.setVisible(true);
		objects.put(platform2.getGUID(), platform2);
		// Create a special platform for murder
		death = new MovingPlatform(guidMaker++, new Rectangle(15, 270, 70, 20), this, motionUpdater, playerDeathZone);
		death.xMin = 15;
		death.xMax = 420;
		death.vMagX = 2;
		death.yMin = 270;
		death.yMax = 270;
		death.setColor(255, 10, 10);
		death.setVisible(true);
		objects.put(death.getGUID(), death);
		
		for(GameObject g : objects.values()){
			if(g.isSpawnable()){
				SpawnEvent e = new SpawnEvent(globalTime.getTime(), 1, g.getGUID().intValue());
				eventManager.raiseSpawnEvent(e);
			}
		}
		
		if(debug){
			p1 = createPlayer(guidMaker++);
			objects.put(p1.getGUID(), p1);
		}
		
		stateLock.release();
		
		size(WIDTH, HEIGHT);
	}
	
	/**
	 * This method draws each frame on each step of the game loop
	 */
	public void draw(){
		//Set the background
		background(25);
		
		try {
			stateLock.acquire();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		// Step the global time up
		globalTime.step();
		sendableEvents.clear();
		for(GameObject g : objects.values()){
			// Handle input
			if(g instanceof Player)
				hidHandler.handleInput((Player) g);
			
			// Move and update
			g.move();
			if(!collisionDetector.collides(g, window)){
				SpawnEvent e = new SpawnEvent(globalTime.getTime(), 2, g.getGUID().intValue());
				eventManager.raiseSpawnEvent(e);
				sendableEvents.add(e);
			}
			// Detect collisions
			for(GameObject g2 : objects.values()){
				if(g.equals(g2))
					continue;
				if(collisionDetector.collides(g, g2)){
					if(((g instanceof Player) && (g2.equals(death))) || ((g2 instanceof Player) && (g.equals(death)))){
						Player p = null;
						if(g instanceof Player)
							p = (Player) g;
						else
							p = (Player) g2;
						
						DeathEvent e = new DeathEvent(globalTime.getTime(), 1, p.getGUID());
						eventManager.raiseDeathEvent(e);
						sendableEvents.add(e);
					}
					else {
						CollisionEvent c = new CollisionEvent(globalTime.getTime(), 2, g.getGUID(), g2.getGUID().intValue());
						eventManager.raiseCollisionEvent(c);
						sendableEvents.add(c);
					}
				}
				else
					collisionDetector.handleNoCollide(g, g2);
			}
		}
		
		// Send Events
		UpdatePacket u = makePacket();
		for(Server s : servers){
			try {
				s.packetLock.acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			s.packet = u;
			s.packetLock.release();
			s.otherPacketLock.release();
			
		}
		
		// Handle Events
		eventManager.handleAllEvents();
		
		stateLock.release();
		
		// Draw each
		for(GameObject g : objects.values())
			g.draw();
	}
	
	/**
	 * This method creates and returns a new player in the game
	 * @return the player created
	 */
	public Player createPlayer(int guid){
		// Make the player
		Player p = new Player(guid, new Rectangle(45, 45), playerSpawn, playerDeathZone, motionUpdater, collisionDetector, this);
		// Set fields
		p.aSetY(Moveable.GRAVITY);
		p.setVisible(true);
		int r = 0;
		int g = 0;
		int b = 0;
		while((r + g + b) < 50){
			r = rand.nextInt(256);
			g = rand.nextInt(256);
			b = rand.nextInt(256);
		}
		p.setColor(r, g, b);
		p.spawn();
		
		return p;
	}
	
	public void handleNewPlayer(NewPlayerEvent e){
		Player p = createPlayer(e.guid);
		objects.put(p.getGUID(), p);
	}
	
	public void handlePlayerQuit(PlayerQuitEvent e){
		objects.remove(new Integer(e.guid));
	}
	
	@Override
	public void keyPressed(KeyEvent k){
		if(debug){
			HIDEvent e = new HIDEvent(globalTime.getTime(), 0, k.getKey(), true, p1.getGUID().intValue());
			eventManager.raiseHIDEvent(e);
		}
	}
	
	@Override
	public void keyReleased(KeyEvent k){
		if(debug){
			HIDEvent e = new HIDEvent(globalTime.getTime(), 0, k.getKey(), false, p1.getGUID().intValue());
			eventManager.raiseHIDEvent(e);
		}
	}
	
	private UpdatePacket makePacket(){
		int size = 0;
		for(GameObject g : objects.values())
			if(g.visible())
				size++;
		UpdatePacket update = new UpdatePacket(size);
		
		int i = 0;
		for(GameObject g : objects.values()){
			if(g.visible()){
				for(int j = 0 ; j < 3 ; j++)
					update.rectColors[i][j] =  g.getColor()[j];;
				Rectangle r = g.getShape();
				update.rectVals[i][0] = r.x;
				update.rectVals[i][1] = r.y;
				update.rectVals[i][2] = r.width;
				update.rectVals[i][3] = r.height;
				i++;
			}
		}
		return update;
	}
}
