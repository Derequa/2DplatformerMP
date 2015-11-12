package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Hashtable;
import events.HIDEvent;
import events.PlayerQuitEvent;

/**
 * This class implements the server thread that handles client requests
 * @author Derek Batts
 *
 */
public class Server implements Runnable{

	// The output stream to the client
	private ObjectOutputStream output;
	private ObjectInputStream input;
	// What worker number am I?
	int myID;
	// The table of worker numbers
	Hashtable<Integer, Boolean> workerNums;
	
	// A lock for waiting on a new Packet
	public Object waitingThing = new Object();
	// The Packet we will send out
	public UpdatePacket packet = null;
	// The GUID of the player we are communicating with
	int playerGUID;
	
	/**
	 * This constructs a Server thread with the given parameters
	 * @param s
	 * @param myID
	 * @param workerNums
	 * @param myPlayer
	 * @throws IOException
	 */
	public Server(Socket s, int myID, Hashtable<Integer, Boolean> workerNums, int playerGUID) throws IOException{
		// Set our fields
		this.output = new ObjectOutputStream(s.getOutputStream());
		this.input = new ObjectInputStream(s.getInputStream());
		this.myID = myID;
		this.workerNums = workerNums;
		this.playerGUID = playerGUID;
	}
	
	/**
	 * This method runs the Server thread
	 */
	@Override
	public void run() {
		try{
			// Loop while the client is connected and hasn't sent a quit signal
			for(String command = (String) input.readObject() ; !command.equals("quit"); command = (String) input.readObject()){
				// Wait for the client to be ready
				if(command.equals("ready")){
					// Synchronize on the lock
					synchronized(waitingThing){
						// Wait for a new packet
						while(packet == null)
							waitingThing.wait();
						// Send and clear the packet
						output.writeObject(new String("shapes"));
						output.writeObject(packet);
						packet = null;
					}
					
					// Signal we are done sending
					output.writeObject(new String("done"));
				}
				// Check if the client is sending input
				else if(command.equals("input")){
					// Reach the character and boolean value
					char c = input.readChar();
					boolean b = input.readBoolean();
					
					// Lock the server state
					GameManager.stateLock.acquire();
					// Raise an appropriate HID event
					HIDEvent e = new HIDEvent(GameManager.globalTime.getTime(), 0, c, b, playerGUID);
					GameManager.eventManager.raiseHIDEvent(e);
					GameManager.stateLock.release();
				}
			}
			// Player quit
			// Lock the server state
			GameManager.stateLock.acquire();
			// Raise an event for the player quitting
			PlayerQuitEvent e = new PlayerQuitEvent(GameManager.globalTime.getTime(), 0, playerGUID);
			GameManager.eventManager.raisePlayerQuitEvent(e);
			GameManager.stateLock.release();
		} catch (IOException e) {
			// Handle sudden disconnection
			try{
				// Lock the server state and raise a quit event
				GameManager.stateLock.acquire();
				PlayerQuitEvent event = new PlayerQuitEvent(GameManager.globalTime.getTime(), 0, playerGUID);
				GameManager.eventManager.raisePlayerQuitEvent(event);
				GameManager.stateLock.release();
			} catch (InterruptedException e2){
				e.printStackTrace();
			}
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
