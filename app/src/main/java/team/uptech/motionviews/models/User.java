package team.uptech.motionviews.models;

import com.xeleb.motionviews.viewmodel.Overlay;

/**
 * Created by Emmanuel on 6/15/17.
 */

public class User extends Overlay {

    private int userID;
    private String username;
    private String url;

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
