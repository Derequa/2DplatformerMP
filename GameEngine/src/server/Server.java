package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Hashtable;
import java.util.concurrent.Semaphore;

import model.Player;

/**
 * This class implements the server thread that handles client requests
 * @author Derek Batts
 *
 */
public class Server implements Runnable{

	// The output stream to the client
	ObjectOutputStream output;
	private ObjectInputStream input;
	// What worker number am I?
	int myID;
	// The table of worker numbers
	Hashtable<Integer, Boolean> workerNums;
	
	public Semaphore packetReady = new Semaphore(0);
	public UpdatePacket send;
	public Player myPlayer = null;
	
	/**
	 * This constructs a Server thread with the given parameters
	 * @param s
	 * @param myID
	 * @param workerNums
	 * @param myPlayer
	 * @throws IOException
	 */
	public Server(Socket s, int myID, Hashtable<Integer, Boolean> workerNums, Player myPlayer) throws IOException{
		this.output = new ObjectOutputStream(s.getOutputStream());
		this.input = new ObjectInputStream(s.getInputStream());
		this.myID = myID;
		this.workerNums = workerNums;
		this.myPlayer = myPlayer;
		GameManager.playerServerMap.put(myPlayer, this);
	}
	
	/**
	 * This method runs the Server thread
	 */
	@Override
	public void run() {
		try{
			while(true){
				// Read in the pressed/released boolean and the character
				Boolean pressed = (Boolean) input.readObject();
				Character in = (Character) input.readObject();
				// Modify our player
				if(pressed.booleanValue())
					myPlayer.setKeyPress(in);
				else
					myPlayer.setKeyRelease(in);
			}
		} catch (IOException e) {
			// Handle sudden disconnection
			// (will be handled in GameManager)
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}
