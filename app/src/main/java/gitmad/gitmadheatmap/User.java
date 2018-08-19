package gitmad.gitmadheatmap;

import java.util.Calendar;
import java.util.Date;

/**
 * Class for app user's.
 * This class is mainly used for storing information in our database in a neat and easy to read manner.
 */
public class User  {

    private String firstName;
    private String lastName;
    private String email;
    private Date joinDate;
    private String username;
//    private LatLng location;
//    private List<User> friends;
//    private LatLng[] mostFrequentedSpots;
//    private boolean nameVisible;

    public User( String firstName, String lastName, String email) {

        this.email = email;
        this.username = emailToUsername( email );
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

    public String getUsername() { return this.username; }

    /**
     * Converts a user's email into an username.
     * @param email A email address.
     * @return The username that would be associated with the email.
     */
    private String emailToUsername( String email ) {
        int at_location = email.indexOf( '@' );
        return email.substring( 0, at_location );
    }

}


