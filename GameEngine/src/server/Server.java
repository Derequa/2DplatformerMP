package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.concurrent.Semaphore;

import events.Event;
import events.HIDEvent;
import events.PlayerQuitEvent;
import model.GameObject;
import model.Player;

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
	
	public Semaphore packetLock = new Semaphore(1);
	public Semaphore otherPacketLock = new Semaphore(0);
	public UpdatePacket packet = null;
	
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
			for(String command = (String) input.readObject() ; !command.equals("quit"); command = (String) input.readObject()){
				if(command.equals("ready")){

					if(packet != null){
						otherPacketLock.acquire();
						packetLock.acquire();
						output.writeObject(new String("shapes"));
						output.writeObject(packet);
						packet = null;
						packetLock.release();
					}
					
					output.writeObject(new String("time"));
					output.writeInt(GameManager.globalTime.getTime());
					output.writeObject(new String("done"));
				}
				else if(command.equals("input")){
					char c = input.readChar();
					boolean b = input.readBoolean();
					
					GameManager.stateLock.acquire();
					HIDEvent e = new HIDEvent(GameManager.globalTime.getTime(), 0, c, b, playerGUID);
					GameManager.eventManager.raiseHIDEvent(e);
					GameManager.stateLock.release();
				}
			}
			// Player quit
			GameManager.stateLock.acquire();
			PlayerQuitEvent e = new PlayerQuitEvent(GameManager.globalTime.getTime(), 0, playerGUID);
			GameManager.eventManager.raisePlayerQuitEvent(e);
			GameManager.stateLock.release();
		} catch (IOException e) {
			// Handle sudden disconnection
			try{
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
