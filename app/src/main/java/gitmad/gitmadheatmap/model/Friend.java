package gitmad.gitmadheatmap.model;

public class Friend {

    private User user;
    private String userHash;

    public Friend( User user ) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
