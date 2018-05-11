package hu.bme.andrismulller.makeithappen_withfriends.model;

import com.orm.SugarRecord;
import com.orm.dsl.Ignore;

/**
 * Created by Muller Andras on 9/18/2017.
 */

public class Alarm extends SugarRecord {
    private String note;
    private long time;
    private int uniqueID;
	private boolean isUsed;
	private boolean isRunning;
	private int type;
    @Ignore
    private long myId;
    private String repeatType;

    public Alarm(){}

    public Alarm(String note, long time, int uniqueID, boolean isUsed, boolean isRunning, String repeatType, int type){
        this.note = note;
        this.time = time;
        this.repeatType = repeatType;
        this.uniqueID = uniqueID;
        this.isUsed = isUsed;
        this.isRunning = isRunning;
        this.type = type;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }


    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getMyId() {
        return myId;
    }

    public void setMyId(long myId) {
        this.myId = myId;
    }

    public String getRepeatType() {
        return repeatType;
    }

    public void setRepeatType(String repeatType) {
        this.repeatType = repeatType;
    }

	public int getUniqueID() {
		return uniqueID;
	}

	public void setUniqueID(int uniqueID) {
		this.uniqueID = uniqueID;
	}


	public boolean isUsed() {
		return isUsed;
	}

	public void setUsed(boolean used) {
		isUsed = used;
	}

	public boolean isRunning() {
		return isRunning;
	}

	public void setRunning(boolean running) {
		isRunning = running;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
}
