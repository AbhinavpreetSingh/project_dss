package org.example.model;

public class LiftRide {
	private int liftID;
	private int time;
	
	
	public LiftRide() {

	}
	
	public LiftRide(int liftID, int time) {
		super();
		this.liftID = liftID;
		this.time = time;
	}

	@Override
	public String toString() {
		return "LiftRide [time=" + time + ", liftID=" + liftID + "]";
	}

	public int getTime() {
		return time;
	}
	public void setTime(int time) {
		this.time = time;
	}
	public int getLiftID() {
		return liftID;
	}
	public void setLiftID(int liftID) {
		this.liftID = liftID;
	}
}


