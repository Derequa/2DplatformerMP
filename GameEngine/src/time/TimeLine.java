package time;

import java.io.Serializable;

/**
 * This class implements a simple time-line for managing events
 * and replays in our game.
 * @author Derek Batts
 *
 */
public class TimeLine implements Serializable{

	/** A constant for the default tic size */
	public static final int DEFAULT = 2;
	/** A constant for a half-speed tic size */
	public static final int HALF = 1;
	/** A constant for a double-speed tic size */
	public static final int DOUBLE = 4;
	/** A constant for a non-progressing tic size */
	public static final int STOP = 0;
	
	private static final long serialVersionUID = -5186951578150891487L;
	// State fields
	private int start;
	private int end;
	private int ticSize;
	private int currentTime;
	boolean isEnded = false;
	boolean direction = true;
	
	/**
	 * This constructs a new TimeLine object around the given parameters.
	 * @param begining The time-stamp for the start of our time-line.
	 * @param ticSize The amount to step by on each time-step.
	 * @param ending The time-stamp for the end of our time-line.
	 */
	public TimeLine(int begining, int ticSize, int ending){
		this.start = begining;
		this.end = ending;
		this.ticSize = ticSize;
		currentTime = start;
		// Decide which direction the time-line is moving
		if(start > end)
			direction = false;
	}
	
	/**
	 * This method increments the current time by the current tic size.
	 */
	public void step(){
		// Check our direction
		if(direction){
			if(currentTime <= end)
				currentTime += ticSize;
			else
				isEnded = true;
		}
		else {
			if(currentTime >= end)
				currentTime -= ticSize;
			else
				isEnded = true;
		}
	}
	
	/**
	 * This method returns the current time.
	 * @return The current time as an integer.
	 */
	public int getTime(){
		return currentTime;
	}
	
	/**
	 * This method returns the current tic size.
	 * @return The current tic size (integer).
	 */
	public int getTicSize(){
		return ticSize;
	}
	
	/**
	 * This method allows the caller to change the current tic size.
	 * @param newtic The new tic size.
	 */
	public void changeTic(int newtic){
		ticSize = newtic;
	}
	
	/**
	 * This method allows the caller to set the current time to a new value.
	 * @param newCurrentTime The new current time to set.
	 */
	public void changeTime(int newCurrentTime){
		currentTime = newCurrentTime;
	}
	
	/**
	 * This method determines if the time-line has progressed to its end-point yet.
	 * @return True if the time-line is at or past its end-point.
	 */
	public boolean isEnded(){
		return isEnded;
	}
	
	/**
	 * This method resets the current time to the starting time.
	 */
	public void restart(){
		currentTime = start;
	}
}
