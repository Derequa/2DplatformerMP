package client;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.concurrent.Semaphore;

import processing.core.PApplet;
import server.UpdatePacket;
import time.TimeLine;

public class ReplayViewer extends PApplet {

	private static final long serialVersionUID = 4070639300389438329L;

	public Semaphore lock = new Semaphore(1);
	public TimeLine timeline;
	public boolean killable = false;
	public boolean playing = false;
	public PriorityQueue<UpdatePacket> frameQueue;
	LinkedList<UpdatePacket> lastFrame = new LinkedList<UpdatePacket>();
	private static final int WIDTH = 500;
	private static final int HEIGHT = 500;
	
	@Override
	public void setup(){
		size(WIDTH, HEIGHT);
		frameQueue = Client.replayQueue;
		timeline = Client.replayTimeLine;
		Client.replayViewer = this;
	}
	
	@Override
	public void draw(){
		if(killable)
			System.exit(0);
		try {
			lock.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		if(playing){
			background(25);
			boolean drewframe = false;
			for(Iterator<UpdatePacket> it = frameQueue.iterator() ; it.hasNext() ;){
					UpdatePacket u = it.next();
					if(!(u.timestamp == timeline.getTime()))
						continue;
					for(int i = 0 ; i < u.numRects ; i++){
						fill(color(u.rectColors[i][0], u.rectColors[i][1], u.rectColors[i][2]));
						rect((float) u.rectVals[i][0], (float) u.rectVals[i][1], (float) u.rectVals[i][2], (float) u.rectVals[i][3]);
					}
					lastFrame.add(u);
					drewframe = true;
			}

			if(!drewframe){
				for(UpdatePacket u : lastFrame){
					for(int i = 0 ; i < u.numRects ; i++){
						fill(color(u.rectColors[i][0], u.rectColors[i][1], u.rectColors[i][2]));
						rect((float) u.rectVals[i][0], (float) u.rectVals[i][1], (float) u.rectVals[i][2], (float) u.rectVals[i][3]);
					}
				}
				lastFrame.clear();
			}
			timeline.step();
		}
		lock.release();

	}

}
