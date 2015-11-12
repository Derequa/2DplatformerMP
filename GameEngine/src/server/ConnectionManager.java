package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Hashtable;
import events.NewPlayerEvent;

/**
 * This class runs in its own thread, and its sole purpose is to make new
 * connections for the server
 * @author Derek Batts
 *
 */
public class ConnectionManager implements Runnable{

	private static final int portNum = 6969;
	// A table for remembering what worker number are available
	private Hashtable<Integer, Boolean> workerNums = new Hashtable<Integer, Boolean>();
	// Our server socket
	private ServerSocket serverSocket;
	// The GameManager object we are tied to
	public GameManager gm = null;
	
	/**
	 * This constructs a ConnectionManager and ties it to a GameManager.
	 * @param parent The GameManager we are tied to.
	 */
	public ConnectionManager(GameManager parent){
		this.gm = parent;
	}
	
	/**
	 * This function runs the thread
	 */
	@Override
	public void run() {
		// Say we are starting up
		System.out.println("Server: Starting connection manager thread...");
		try {
			// Open the server socket
			serverSocket = new ServerSocket(portNum);
			// Infinitely loop, waiting for clients
			for(Socket newSocket = serverSocket.accept() ; true ; newSocket = serverSocket.accept()){
				// Add the socket to the list of sockets opened
				GameManager.clients.add(newSocket);
				// Find an available worker number
				int num;
				for(num = 0 ; workerNums.containsKey(new Integer(num)) && workerNums.get(new Integer(num)).booleanValue() ; num++);
				// Add the worker number as being used
				workerNums.put(new Integer(num), new Boolean(true));
				
				// Lock the game state
				GameManager.stateLock.acquire();
				// Make new player
				int newPlayerGuid = GameManager.guidMaker++;
				NewPlayerEvent e = new NewPlayerEvent(GameManager.globalTime.getTime(), 0, newPlayerGuid);
				// Make associated event
				GameManager.eventManager.raiseNewPlayerEvent(e);
				// Unlock game state
				GameManager.stateLock.release();
				
				// Make a new Server thread
				Server newServer = new Server(newSocket, num, workerNums, newPlayerGuid);
				Thread newThread = new Thread(newServer);
				// Add the thread to the list of threads
				GameManager.servers.add(newServer);
				// Say we accepted a new connection
				System.out.println("Server: Accepted new connection!\nServer: Assigning worker #" + num + "...");
				// Start the thread
				newThread.start();
			}
		} catch (Exception e) {
			System.out.println("Connection manager interrupted!");
			e.printStackTrace();
			try {
				serverSocket.close();
			} catch (IOException e1) {
				e.printStackTrace();
			}
			return;
		}
	}
}
