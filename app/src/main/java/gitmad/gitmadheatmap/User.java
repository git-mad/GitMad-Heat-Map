package gitmad.gitmadheatmap;

import com.google.android.gms.maps.model.LatLng;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class User  {

    private String username;
    private String password;
    private String firstName;
    private String middleName;
    private String lastName;
    private String email;
    private Date joinDate;
    private LatLng location;
    private List<User> friends;
    private LatLng[] mostFrequentedSpots;
    private boolean nameVisible;

    public User(String username, String password, String email,
                String firstName, String middleName, String lastName) {

        this.username = username;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.joinDate = Calendar.getInstance().getTime();

    }

    public String getUsername(){
        return this.username;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getMiddleName() {
        return this.middleName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public String getEmail() { return this.email; }


}


