package client;

import java.awt.Rectangle;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.concurrent.Semaphore;

import processing.core.PApplet;
import processing.event.KeyEvent;
import model.*;
import events.*;
import server.GameStatePacket;
import server.UpdatePacket;
import time.TimeLine;

/**
 * This class implements the client side of our game
 * @author Derek Batts
 *
 */
public class Client extends PApplet {
	
	// Window stuff
	private static final int WIDTH = 500;
	private static final int HEIGHT = 500;
	
	// Port number for the server
	private static final int portNum = 6969;
	// An important thing
	private static final long serialVersionUID = -689959018126573424L;
	
	// Networking objects
	private static Socket mySocket;
	private static ObjectOutputStream output;
	private static ObjectInputStream input;
	
	public Semaphore inputLock = new Semaphore(1);
	public static TimeLine globalTime = new TimeLine(0, 2, Integer.MAX_VALUE);

	
	/**
	 * This method sets up our sketch
	 */
	public void setup(){
		//Set size
		size(WIDTH, HEIGHT);
		
		// Connect to server
		try {
			mySocket = new Socket("127.0.0.1", portNum);
			output = new ObjectOutputStream(mySocket.getOutputStream());
			input = new ObjectInputStream(mySocket.getInputStream());
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	/**
	 * This method handles drawing every frame
	 */
	public void draw(){
		// Set background
		background(25);
		UpdatePacket u = null;
		
		try{
			inputLock.acquire();
			globalTime.step();
			output.writeObject(new String("ready"));
			
			for(String state = (String) input.readObject() ; !state.equals("done") ; state = (String) input.readObject()){
				if(state.equals("shapes"))
					u = (UpdatePacket) input.readObject();
				else if(state.equals("time")){
					int serverTime = input.readInt();
					if(serverTime != globalTime.getTime())
						globalTime.changeTime(serverTime);
				}
			}
		} catch (Exception e){
			e.printStackTrace();
		}
		if(u != null){
			for(int i = 0 ; i < u.numRects ; i++){
				fill(color(u.rectColors[i][0], u.rectColors[i][1], u.rectColors[i][2]));
				rect((float) u.rectVals[i][0], (float) u.rectVals[i][1], (float) u.rectVals[i][2], (float) u.rectVals[i][3]);
			}
		}
		
		inputLock.release();
		
	}
	
	@Override
	/**
	 * This method responds to a user pressing a key
	 */
	public void keyPressed(KeyEvent k){
		// Try to send the key press
		try {
			inputLock.acquire();
			output.writeObject(new String("input"));
			output.writeChar(k.getKey());
			output.writeBoolean(true);
			inputLock.release();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}
	
	@Override
	/**
	 * This method responds to a key being released
	 */
	public void keyReleased(KeyEvent k){
		// Try to send the key release
		try {
			inputLock.acquire();
			output.writeObject(new String("input"));
			output.writeChar(k.getKey());
			output.writeBoolean(false);
			inputLock.release();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}