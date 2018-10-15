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

        // TODO 2: Create a new FriendsAdapter instance.

        // TODO 5: Grab the listView from our fragment view and set its adapter to the adapter we made.

        // TODO 6(optional): Add a case that displays a text view indicating you don't have any friends added when there are no friends in the ArrayList.

        return v;
    }
}
