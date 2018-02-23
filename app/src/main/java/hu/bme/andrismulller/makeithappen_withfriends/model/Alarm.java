package hu.bme.andrismulller.makeithappen_withfriends.model;

import com.orm.SugarRecord;
import com.orm.dsl.Ignore;

/**
 * Created by Muller Andras on 9/18/2017.
 */

public class Alarm extends SugarRecord {
    private String note;
    private long time;
    @Ignore
    private long myId;
    private String repeatType;

    public Alarm(){}

    public Alarm(String note, long time, String repeatType){
        this.note = note;
        this.time = time;
        this.repeatType = repeatType;
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
}
