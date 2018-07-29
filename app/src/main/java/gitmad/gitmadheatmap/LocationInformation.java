package gitmad.gitmadheatmap;

import com.google.android.gms.maps.model.LatLng;

import java.text.DateFormat;
import java.util.Date;

public class LocationInformation {
    private LatLng location;
    private String currentTime;

    public LocationInformation( LatLng location ) {
        this.location = location;
        currentTime = DateFormat.getDateTimeInstance().format( new Date() );
    }

    public LatLng getLocation() {
        return this.location;
    }

    public String getCurrentTime() {
        return this.currentTime;
    }
}
