package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.PriorityQueue;
import java.util.concurrent.Semaphore;
import processing.core.PApplet;
import processing.event.KeyEvent;
import events.*;
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
	
	// The state lock for the client
	public Semaphore inputLock = new Semaphore(1);
	// The client's time-line
	public TimeLine globalTime = new TimeLine(0, 2, Integer.MAX_VALUE);
	// An event manager for stuff
	public EventManager eventManager = new EventManager();
	
	// A queue of frames to remember for a replay
	public static PriorityQueue<UpdatePacket> replayQueue = new PriorityQueue<UpdatePacket>();
	// A time-line for a replay
	public static TimeLine replayTimeLine;
	// A flag to signal if we are recording
	public boolean recordingReplay = false;
	// A time-stamp for the beginning of the replay
	public int replayStart;
	// A time-stamp for the end of the replay
	public int replayEnd;
	// A PApplet to watch the replay on
	public static ReplayViewer replayViewer;
	// The GUI for our replay thingy
	ClientManager parent;
	
	/**
	 * This method sets up our sketch
	 */
	public void setup(){
		//Set size
		size(WIDTH, HEIGHT);
		
		parent = new ClientManager(this);
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
		eventManager.registerReplayEvents(this);
	}
	
	/**
	 * This method handles drawing every frame
	 */
	public void draw(){
		// Set background
		background(25);
		UpdatePacket u = null;
		
		try{
			// Lock the state
			inputLock.acquire();
			// Step the time-line
			globalTime.step();
			// Signal the server we are ready for a packet
			output.writeObject(new String("ready"));
			
			// Read stuff until the server signals its done
			for(String state = (String) input.readObject() ; !state.equals("done") ; state = (String) input.readObject()){
				// Read the next frame
				if(state.equals("shapes"))
					u = (UpdatePacket) input.readObject();
			}
		} catch (Exception e){
			e.printStackTrace();
		}
		if(u != null){
			// Update our time
			if(globalTime.getTime() != u.timestamp)
				globalTime.changeTime(u.timestamp);
			// Draw all the stuff
			for(int i = 0 ; i < u.numRects ; i++){
				fill(color(u.rectColors[i][0], u.rectColors[i][1], u.rectColors[i][2]));
				rect((float) u.rectVals[i][0], (float) u.rectVals[i][1], (float) u.rectVals[i][2], (float) u.rectVals[i][3]);
			}
			// Remember the frame if we are recording
			if(recordingReplay)
				replayQueue.add(u);
		}
		
		// Handle any replay events
		eventManager.handleAllEvents();
		// Update GUI buttons
		parent.setButtons();
		// Unlock our state
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
	
	/**
	 * This method handles a starting the recording of a replay.
	 * @param e The event that signaled us.
	 */
	public void handleReplayEvent(ReplayEvent e){
		// Only start recording if we aren't recording
		if(!recordingReplay){
			// Clear the queue
			replayQueue.clear();
			// Set the flag
			recordingReplay = true;
			// Mark the start
			replayStart = globalTime.getTime();
			// Signal the viewer (if any) to wait while we record
			if(replayViewer != null){
				try {
					replayViewer.lock.acquire();
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				replayViewer.frameQueue.clear();
				replayViewer.timeline.changeTic(TimeLine.STOP);
				replayViewer.lock.release();
			}
		}
	}
	
	/**
	 * This method handle the stopping the recording of a replay.
	 * @param e The event that signaled us.
	 */
	public void handleStopReplayEvent(StopReplayEvent e){
		// Only stop if we are recording
		if(recordingReplay){
			// Set the flag and mark the time
			recordingReplay = false;
			replayEnd = globalTime.getTime();
			// Make a new time-line
			replayTimeLine = new TimeLine(replayStart, TimeLine.DEFAULT, replayEnd);
			// Make a new viewer if none exists
			if((replayViewer == null))
				PApplet.main("client.ReplayViewer");
			// Update our viewer
			else{
				try {
					// Lock the viewer sate
					replayViewer.lock.acquire();
					// Set the new time-line and frame queue
					replayViewer.timeline = new TimeLine(replayStart, TimeLine.DEFAULT, replayEnd);
					replayViewer.frameQueue = replayQueue;
					replayViewer.lock.release();
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * This handles changing the speed of a replay's playback.
	 * @param e The event describing the speed change.
	 */
	public void handleReplaySpeedChangeEvent(ReplaySpeedChangeEvent e){
		// Only change the playback speed if we are not recording and there is a viewer.
		if(!recordingReplay && (replayViewer != null)){
			try {
				// Lock the viewer
				replayViewer.lock.acquire();
				// Set the new speed
				replayViewer.timeline.changeTic(e.newSpeed);
				replayViewer.lock.release();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	/**
	 * This handles restarting playback of a replay.
	 * @param e The event that signaled us.
	 */
	public void handleReplayRestartEvent(RestartReplayEvent e){
		// Only restart if we are not recording and there is a viewer
		if(!recordingReplay && (replayViewer != null)){
			try {
				// Lock the viewer
				replayViewer.lock.acquire();
				// Restart the viewer's time-line
				replayViewer.timeline.restart();
				replayViewer.lock.release();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	/**
	 * This handles playing/pausing a replay.
	 * @param e The event telling us to play/pause.
	 */
	public void handlePlayPauseReplayEvent(PlayPauseReplayEvent e){
		// Only play/pause if we are not recording and there is a viewer
		if(!recordingReplay && (replayViewer != null)){
			try {
				// Lock the viewer
				replayViewer.lock.acquire();
				// Set the playing flag to what the event says
				replayViewer.playing = e.play;
				replayViewer.lock.release();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}
	}
}