package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import processing.core.PApplet;
import processing.event.KeyEvent;
import server.UpdatePacket;

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
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * This method handles drawing every frame
	 */
	public void draw(){
		// Set background
		background(25);
		
		// Initialize a new UpdatePacket
		UpdatePacket p = null;
		try {
			//Try to read in the packet
			p = (UpdatePacket) input.readObject();
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
		// Check for null
		if(p == null)
			throw new NullPointerException("Update Packet was null");
		
		// Draw each rectangle in the packet
		for(int i = 0 ; i < p.numRects ; i++){
			int r = p.rectColors[i][0];
			int g = p.rectColors[i][1];
			int b = p.rectColors[i][2];
			
			// Set the color
			fill(color(r, g, b));
			
			int x = p.rectVals[i][0];
			int y = p.rectVals[i][1];
			int w = p.rectVals[i][2];
			int h = p.rectVals[i][3];
			
			// Draw the rectangle
			rect((float) x, (float) y, (float) w, (float) h);
			
		}
	}
	
	@Override
	/**
	 * This method responds to a user pressing a key
	 */
	public void keyPressed(KeyEvent k){
		// Try to send the key press
		try {
			output.writeObject(new Boolean(true));
			output.writeObject(new Character(k.getKey()));
		} catch (IOException e) {
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
			output.writeObject(new Boolean(false));
			output.writeObject(new Character(k.getKey()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}