package gitmad.gitmadheatmap;

import com.google.android.gms.maps.model.LatLng;

import java.text.DateFormat;
import java.util.Date;

/**
 * Class for storing location information.
 * This is mainly used so we can nicely store information within our database.
 */
public class LocationInformation {
    private LatLng location;
    private String currentTime;
    private String username;

    public LocationInformation(LatLng location, String username) {
        this.location = location;
        currentTime = DateFormat.getDateTimeInstance().format(new Date());
        this.username = username;
    }

    public LatLng getLocation() {
        return this.location;
    }

    public String getCurrentTime() {
        return this.currentTime;
    }

    public String getUsername() {
        return this.username;
    }
}
