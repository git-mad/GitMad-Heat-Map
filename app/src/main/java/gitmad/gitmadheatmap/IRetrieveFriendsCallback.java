package gitmad.gitmadheatmap;

import java.util.ArrayList;

import gitmad.gitmadheatmap.model.Friend;

public interface IRetrieveFriendsCallback {
    void onFinish(ArrayList<Friend> friends);
}
