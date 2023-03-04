package org.example.model;

public class SkierLiftRideEvent {
	private final int skierId;
    private final int resortId;
    private final int liftId;
    private final int seasonId;
    private final int dayId;
    private final int time;

    public SkierLiftRideEvent(int skierId, int resortId, int liftId, int seasonId, int dayId, int time) {
        this.skierId = skierId;
        this.resortId = resortId;
        this.liftId = liftId;
        this.seasonId = seasonId;
        this.dayId = dayId;
        this.time = time;
    }

    public int getSkierId() {
        return skierId;
    }

    public int getResortId() {
        return resortId;
    }

    public int getLiftId() {
        return liftId;
    }

    public int getSeasonId() {
        return seasonId;
    }

    public int getDayId() {
        return dayId;
    }

    public int getTime() {
        return time;
    }
}
