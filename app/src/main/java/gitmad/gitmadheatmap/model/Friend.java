package gitmad.gitmadheatmap.model;

import java.util.UUID;

public class Friend {

    private String firstName;
    private String lastName;
    private String email;
    private String hash;

    public Friend( String firstName, String lastName, String email, String hash ) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.hash = hash;
    }

    public Friend( String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.hash = createHash();
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getHash() {
        return hash;
    }

    /**
     * Create a new userId SharedPreference value.
     * This value is used so that we can still upload user's locations anonymously.
     */
    private String createHash() {
        return UUID.randomUUID().toString().substring(0, 10);
    }
}

