package gitmad.gitmadheatmap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

import gitmad.gitmadheatmap.model.Friend;

public class FriendsAdapter extends ArrayAdapter {

    public FriendsAdapter(Context context, ArrayList<Friend> friends ) {
        super( context, 0, friends );
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent ) {
        // Get the data item for this position.
        Friend friend = (Friend) getItem( position );

        // Check if the existing view is being reused, otherwise inflate the view
        // TODO 3: Create a new layout to inflate that will represent each friend. I recommend having views for their name and buttons for looking them up on the map and removing them.
        // HINT: There are some images that you can use in your drawable resource directory that can be ;)
        if( convertView == null ) {
            // TODO 3.1: Un-comment this line and add your layout resource here.
//            convertView = LayoutInflater.from( getContext() ).inflate( ###Layout_Resource###, parent, false );
        }

        // TODO 4: Grab the views from the layout you created and set their values.

        return convertView;
    }
}
