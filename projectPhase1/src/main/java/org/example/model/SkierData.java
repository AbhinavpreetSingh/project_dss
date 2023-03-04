package org.example.model;

public class SkierData {
    private String resortID;
    private String seasonID;
    private String dayID;
    private int skierID;
    
    public SkierData(String resortID, String seasonID, String dayID, int skierID) {
		super();
		this.resortID = resortID;
		this.seasonID = seasonID;
		this.dayID = dayID;
		this.skierID = skierID;
	}

	@Override
	public String toString() {
		return "SkierData [resortID=" + resortID + ", seasonID=" + seasonID + ", dayID=" + dayID + ", skierCount="
				+ skierID  + "]";
	}

    public String getResortID() {
        return resortID;
    }

    public void setResortID(String resortID) {
        this.resortID = resortID;
    }

    public String getSeasonID() {
        return seasonID;
    }

    public void setSeasonID(String seasonID) {
        this.seasonID = seasonID;
    }

    public String getDayID() {
        return dayID;
    }

    public SkierData() {
		super();
	}

	public void setDayID(String dayID) {
        this.dayID = dayID;
    }

    public int getSkierID() {
        return skierID;
    }

    public void setSkierCount(int skierID) {
        this.skierID = skierID;
    }
}
