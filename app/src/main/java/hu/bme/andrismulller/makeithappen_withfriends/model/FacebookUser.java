package hu.bme.andrismulller.makeithappen_withfriends.model;

import com.orm.SugarRecord;
import com.orm.dsl.Ignore;

/**
 * Created by Muller Andras on 10/11/2017.
 */

public class FacebookUser extends SugarRecord{
    @Ignore
    private long myId;
    private String userName;
    private String facebookId;

    public FacebookUser(){}

    public FacebookUser(String userName, String facebookId) {
        this.userName = userName;
        this.facebookId = facebookId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFacebookId() {
        return facebookId;
    }

    public void setFacebookId(String facebookId) {
        this.facebookId = facebookId;
    }

    public long getMyId() {
        return myId;
    }

    public void setMyId(long myId) {
        this.myId = myId;
    }

}
