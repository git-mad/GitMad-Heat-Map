package gitmad.gitmadheatmap.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

/**
 * Class for app user's.
 * This class is mainly used for storing information in our database in a neat and easy to read manner.
 */
public class User {

    private String firstName;
    private String lastName;
    private String email;
    private Date joinDate;
    private String username;
    private String hash;
    private String friends;
//    private LatLng location;
//    private List<User> friends;
//    private LatLng[] mostFrequentedSpots;
//    private boolean nameVisible;

    public User(String firstName, String lastName, String email) {
        this.email = email;
        this.username = emailToUsername(email);
        this.firstName = firstName;
        this.lastName = lastName;
        this.joinDate = Calendar.getInstance().getTime();
        this.hash = createUserHash();
        this.friends = "";
    }

    public User(String firstName, String lastName, String email, String hash, String friends) {
        this.email = email;
        this.username = emailToUsername(email);
        this.firstName = firstName;
        this.lastName = lastName;
        this.joinDate = Calendar.getInstance().getTime();
        this.hash = hash;
        this.friends = friends;
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

    public String getUsername() {
        return this.username;
    }

    public String getHash() { return this.hash; }

    public String getFriends() { return this.friends; }

    public void addFriend( String friend ) { this.friends += "," + friend; }

    public ArrayList<String> getFriendsSet() {
        return new ArrayList<>(Arrays.asList(friends.split(",")));
    }

    /**
     * Converts a user's email into an username.
     *
     * @param email A email address.
     * @return The username that would be associated with the email.
     */
    private String emailToUsername(String email) {
        return email.substring(0, email.indexOf('@'));
    }

    /**
     * Create a new userId SharedPreference value.
     * This value is used so that we can still upload user's locations anonymously.
     */
    private String createUserHash() {
        return UUID.randomUUID().toString().substring(0, 10);
    }

}
