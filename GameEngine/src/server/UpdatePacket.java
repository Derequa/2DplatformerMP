package server;

import java.io.Serializable;

/**
 * This class wraps up data needed for sending world state info to a client
 * @author Derek Batts
 *
 */
public class UpdatePacket implements Serializable, Comparable<UpdatePacket> {

	// VERY IMPORTANT
	private static final long serialVersionUID = -4278372982831805939L;
	// Server time when this packet was made and sent
	public int timestamp;
	// How many rectangles are there?
	public int numRects;
	// A 2D array for each rectangle's x, y, width, and height
	public int[][] rectVals;
	// A 2D array for each rectangle's RGB color info
	public int[][] rectColors;

	// The constructor initializes the arrays
	public UpdatePacket(int numRects, int timestamp){
		this.numRects = numRects;
		this.timestamp = timestamp;
		rectVals = new int[numRects][4];
		rectColors = new int[numRects][3];
	}

	@Override
	public int compareTo(UpdatePacket o) {
		return (new Integer(timestamp)).compareTo(new Integer(o.timestamp));
	}
}
