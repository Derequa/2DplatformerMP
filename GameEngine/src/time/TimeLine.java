package time;

import java.io.Serializable;

public class TimeLine implements Serializable{

	public static final int DEFAULT = 2;
	public static final int HALF = 1;
	public static final int DOUBLE = 4;
	public static final int STOP = 0;
	
	private static final long serialVersionUID = -5186951578150891487L;
	private int start;
	private int end;
	private int ticSize;
	private int currentTime;
	boolean isEnded = false;
	boolean direction = true;
	
	public TimeLine(int begining, int ticSize, int ending){
		this.start = begining;
		this.end = ending;
		this.ticSize = ticSize;
		currentTime = start;
		
		if(start > end)
			direction = false;
	}
	
	public void step(){
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
	
	public int getTime(){
		return currentTime;
	}
	
	public int getTicSize(){
		return ticSize;
	}
	
	public void changeTic(int newtic){
		ticSize = newtic;
	}
	
	public void changeTime(int newCurrentTime){
		currentTime = newCurrentTime;
	}
	
	public boolean isEnded(){
		return isEnded;
	}
	
	public void restart(){
		currentTime = start;
	}
}
