package gitmad.gitmadheatmap;

import java.util.Calendar;
import java.util.Date;

public class User  {

    private String firstName;
    private String lastName;
    private String email;
    private Date joinDate;
//    private LatLng location;
//    private List<User> friends;
//    private LatLng[] mostFrequentedSpots;
//    private boolean nameVisible;

    public User( String firstName, String lastName, String email) {

        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.joinDate = Calendar.getInstance().getTime();

    }

    public String getEmail() {
        return this.email;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public Date getJoinDate() {
        return this.joinDate;
    }

}


