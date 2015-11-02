package server;

import java.awt.Rectangle;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.Semaphore;

import model.*;
import processing.core.PApplet;

/**
 * This class displays and updates the game world. It also starts up server-client communication
 * and sends out world state to all clients on each step.
 * @author Derek Batts
 *
 */
public class GameManager extends PApplet {
	
	// Important thing
	private static final long serialVersionUID = 1227957981784275051L;
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
	public static Semaphore playersLock = new Semaphore(1);
	
	// A list of all the players in the game
	protected static LinkedList<Player> players = new LinkedList<Player>();
	// A list of all the flying boxes in the game
	protected static ArrayList<Box> flyingBoxes = new ArrayList<Box>();
	// A list of all the static objects in the game
	protected static LinkedList<GameObject> staticObjects = new LinkedList<GameObject>();
	// A list of all the visible platforms
	protected static LinkedList<StaticRectangle> platforms = new LinkedList<StaticRectangle>();
	// An array of all the walls
	protected static StaticRectangle[] walls = new StaticRectangle[4];
	
	// A spawn for players (and boxes)
	protected static Spawn playerSpawn;
	// A death zone for players
	protected static Spawn playerDeathZone;
	// A collision detector
	protected static Collider collisionDetector = new Collider();
	// A motion updater
	protected static Mover motionUpdater = new Mover();
	
	// Window size stuff
	private static final int WIDTH = 500;
	private static final int HEIGHT = 500;
	
	// A counter for players
	private static int playerNum = 0;
	// A place to print (deprecated)
	protected static OutputStream console = System.out;
	// A rectangle defining the window
	Rectangle window = new Rectangle(0, 0, WIDTH, HEIGHT);
	// A very special platform for killing players
	private StaticRectangle death;
	// The direction the very special platform is moving
	private boolean deathrection = true;
	
	/**
	 * This method sets up the game
	 */
	public void setup(){
		// Start listening for new connections
		listenerThread.start();
		
		// Create spawn and death zones
		Rectangle spawnBounds = new Rectangle(2, 30, 30, 30);
		Rectangle despawnBounds = new Rectangle(15, 260, 70, 20);
		playerSpawn = new Spawn(spawnBounds);
		playerDeathZone = new Spawn(despawnBounds);
		
		// Create all the Boxes
		for(int i = 0 ; i < 4 ; i++){
			Rectangle box = new Rectangle((WIDTH / 2) - 30, 10, 30, 30);
			Box b = new Box(box, playerSpawn, playerDeathZone, motionUpdater, collisionDetector, this);
			b.setColor(255, 255, 255);
			b.setVisible(true);
			b.vSet(rand.nextInt(4),rand.nextInt(4));
			flyingBoxes.add(b);
		}
		
		// Create all the walls
		walls[0] = new StaticRectangle(new Rectangle(0, 0, WIDTH, 1), this);
		walls[1] = new StaticRectangle(new Rectangle(0, HEIGHT, WIDTH, 1), this);
		walls[2] = new StaticRectangle(new Rectangle(WIDTH, 0, 1, HEIGHT), this);
		walls[3] = new StaticRectangle(new Rectangle(0, 0, 1, HEIGHT), this);
		for(int i = 0 ; i < 4 ; i++)
			staticObjects.add(walls[i]);
		
		// Create two platforms  to jump on
		StaticRectangle platform1 = new StaticRectangle(new Rectangle(350 - 85, 350 + 20, 85, 20), this);
		platform1.setColor(255, 255, 255);
		platform1.setVisible(true);
		staticObjects.add(platform1);
		StaticRectangle platform2 = new StaticRectangle(new Rectangle(10, 125 + 20, 85, 20), this);
		platform2.setColor(255, 255, 255);
		platform2.setVisible(true);
		staticObjects.add(platform2);
		// Create a special platform for murder
		death = new StaticRectangle(new Rectangle(15, 270, 70, 20), this);
		death.setColor(255, 10, 10);
		death.setVisible(true);
		
		// Add platforms to lists
		staticObjects.add(death);
		platforms.add(platform1);
		platforms.add(platform2);
		platforms.add(death);
		
		size(WIDTH, HEIGHT);		
	}
	
	/**
	 * This method draws each frame on each step of the game loop
	 */
	public void draw(){
		//Set the background
		background(25);
		
		// Create iterators for objects
		Iterator<GameObject> platformIterator;
		Iterator<Player> playerIterator = players.iterator();
		Iterator<Box> boxIterator = flyingBoxes.iterator();
		
		//Motion Update/Handle inputs
		while(playerIterator.hasNext()){
			Player p = playerIterator.next();
			handleInput(p);
			p.move();
		}
		while(boxIterator.hasNext())
			boxIterator.next().move();
		
		//Move death platform manually
		if(deathrection){
			death.posSetX(death.posGetX() + 1);
			if(death.posGetX() > 400)
				deathrection = false;
		}
		else {
			death.posSetX(death.posGetX() - 1);
			if(death.posGetX() < 15)
				deathrection = true;
		}
		//Move death de-spawner
		playerDeathZone.moveSpawn(death.posGetX(), death.posGetY());
		
		// Player Collisions
		playerIterator = players.iterator();
		while(playerIterator.hasNext()){
			Player p = playerIterator.next();
			Collider c = p.getCollider();
			
			if(p.inDeathZone())
				p.spawn();
			platformIterator = staticObjects.iterator();
			while(platformIterator.hasNext())
				c.collide(p, platformIterator.next());
			
			if(!p.getShape().intersects(window))
				p.spawn();
		}
		
		// Box Collisions
		boxIterator = flyingBoxes.iterator();
		while(boxIterator.hasNext()){
			platformIterator = staticObjects.iterator();
			Box b = boxIterator.next();
			Collider c = b.getCollider();
			while(platformIterator.hasNext())
				c.collide(b, platformIterator.next());
			playerIterator = players.iterator();
			while(playerIterator.hasNext())
				c.collide(b, playerIterator.next());
			Iterator<Box> otherBoxes = flyingBoxes.iterator();
			while(otherBoxes.hasNext()){
				Box other = otherBoxes.next();
				if(!b.getShape().equals(other.getShape()))
					c.collide(b, other);
			}
			
			if(!b.getShape().intersects(window))
				b.spawn();
		}
		
		// Reset Iterators
		playerIterator = players.iterator();
		boxIterator = flyingBoxes.iterator();
		platformIterator = staticObjects.iterator();
		
		// Draw stuff
		while(playerIterator.hasNext())
			playerIterator.next().draw();
		while(boxIterator.hasNext())
			boxIterator.next().draw();
		while(platformIterator.hasNext())
			platformIterator.next().draw();
		
		// Send "frame" to clients
		sendShapes();
	}
	
	/**
	 * This method handles input for a given player
	 * @param p The player to handle input for
	 */
	private void handleInput(Player p){
		// Check for jump key press and make sure we aren't hitting a ceiling
		if((p.keyIsPressed('w') || p.keyIsPressed('W')) && !(p.isCollided() && !p.isOnFloor()))
			p.vSetY(-3.0f);
		
		// Booleans for left and right key press
		boolean left = (p.keyIsPressed('a') || p.keyIsPressed('A'));
		boolean right = (p.keyIsPressed('d') || p.keyIsPressed('D'));
		
		// Only move left/right if its the only one pressed and we aren't hitting a wall
		if(left && !right && !(p.isCollided() && !p.isOnFloor()) && !p.collidedFromRight)
			p.vSetX(-3.0f);
		else if(right && !left && !(p.isCollided() && !p.isOnFloor()) && !p.collidedFromLeft)
			p.vSetX(3.0f);
	}
	
	/**
	 * This method packs all the objects in our world to a special game object
	 * to send to the client.
	 */
	private void sendShapes(){
		// Create iterators
		Iterator<Player> playerIterator = players.iterator();
		Iterator<Box> boxIterator = flyingBoxes.iterator();
		Iterator<StaticRectangle> platformIterator = platforms.iterator();
		
		// Lock the list of players
		try {
			playersLock.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		// Caclulate how many things there are to send
		int numRects = players.size() + flyingBoxes.size() + platforms.size();
		UpdatePacket u = new UpdatePacket(numRects);
		int i = 0;
		
		// Add in player x, y, width, height, and RGB data
		for(; (i < numRects) && playerIterator.hasNext() ; i++){
			Player p = playerIterator.next();
			u.rectVals[i][0] = p.getShape().x;
			u.rectVals[i][1] = p.getShape().y;
			u.rectVals[i][2] = p.getShape().width;
			u.rectVals[i][3] = p.getShape().height;
			u.rectColors[i][0] = p.getColor()[0];
			u.rectColors[i][1] = p.getColor()[1];
			u.rectColors[i][2] = p.getColor()[2];
		}
		// Release the lock
		playersLock.release();
		
		// Add in box x, y, width, height, and RGB data
		for(; (i < numRects) && boxIterator.hasNext() ; i++){
			Box b = boxIterator.next();
			u.rectVals[i][0] = b.getShape().x;
			u.rectVals[i][1] = b.getShape().y;
			u.rectVals[i][2] = b.getShape().width;
			u.rectVals[i][3] = b.getShape().height;
			u.rectColors[i][0] = b.getColor()[0];
			u.rectColors[i][1] = b.getColor()[1];
			u.rectColors[i][2] = b.getColor()[2];
		}
		
		// Add in player x, y, width, height, and RGB data
		for(; (i < numRects) && platformIterator.hasNext() ; i++){
			StaticRectangle r = platformIterator.next();
			u.rectVals[i][0] = r.getShape().x;
			u.rectVals[i][1] = r.getShape().y;
			u.rectVals[i][2] = r.getShape().width;
			u.rectVals[i][3] = r.getShape().height;
			u.rectColors[i][0] = r.getColor()[0];
			u.rectColors[i][1] = r.getColor()[1];
			u.rectColors[i][2] = r.getColor()[2];
		}
		
		//Send to clients
		Iterator<Server> serverIterator = servers.iterator();
		while(serverIterator.hasNext()){
			Server s = serverIterator.next();
			try {
				s.output.writeObject(u);
			} catch (IOException e) {
				s.workerNums.put(new Integer(s.myID), false);
				System.out.println("Server worker #" + s.myID + ": Client disconnected!");
				clients.remove(s);
				players.remove(s.myPlayer);
				playerServerMap.remove(s.myPlayer);
				servers.remove(s);
				
			};
		}
	}
	
	/**
	 * This method creates and returns a new player in the game
	 * @return the player created
	 */
	public Player createPlayer(){
		// Make the player
		Player p = new Player(new Rectangle(45, 45), playerSpawn, playerDeathZone, motionUpdater, collisionDetector, this, playerNum++);
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
		
		// Lock the list
		try {
			playersLock.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		// Add the new player and unlock
		players.add(p);
		playersLock.release();
		
		return p;
	}
}
