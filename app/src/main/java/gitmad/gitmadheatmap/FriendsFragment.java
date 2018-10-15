package gitmad.gitmadheatmap;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import gitmad.gitmadheatmap.model.Friend;

public class FriendsFragment extends Fragment {


    public static FriendsFragment newInstance() {
        return new FriendsFragment();
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_friends, container, false);

        // TODO 1: Create a new ArrayList of type Friend with multiple entries.
        ArrayList<Friend> friends = new ArrayList<>();

        friends.add( new Friend("John", "Doe", "jDoe@gmail.com" ) );
        friends.add( new Friend("Kevin", "Bacon", "kBacon@gmail.com" ) );
        friends.add( new Friend("Samantha", "Stewart", "sStewart@gmail.com" ) );
        friends.add( new Friend("Roxy", "Epoxy", "rEpoxy@gmail.com" ) );

        // TODO 2: Create a new FriendsAdapter instance.
        FriendsAdapter friendsAdapter = new FriendsAdapter( getContext(), friends );

        // TODO 5: Grab the listView from our fragment view and set its adapter to the adapter we made.
        ListView listView = v.findViewById( R.id.friends_list_friends );
        listView.setAdapter( friendsAdapter );

        // TODO 6(optional): Add a case that displays a text view indicating you don't have any friends added when there are no friends in the ArrayList.
        // I would comment all the adds above to test this case.
        if( friends.isEmpty() ) {
            TextView noFriends = v.findViewById(R.id.friends_text_no_friends);
            noFriends.setVisibility( View.VISIBLE );
        }

        return v;
    }
}
