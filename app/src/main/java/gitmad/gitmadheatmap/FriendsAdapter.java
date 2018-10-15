package gitmad.gitmadheatmap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.ArrayList;

import gitmad.gitmadheatmap.model.Friend;
import gitmad.gitmadheatmap.model.User;

public class FriendsAdapter extends ArrayAdapter {

    public FriendsAdapter(Context context, ArrayList<Friend> friends ) {
        super( context, 0, friends );
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent ) {
        // Get the data item for this position.
        Friend friend = (Friend) getItem( position );

        // Check if the existing view is being reused, otherwise inflate the view
        if( convertView == null ) {
            convertView = LayoutInflater.from( getContext() ).inflate( R.layout.friend_user_item, parent, false );
        }

        TextView userName = convertView.findViewById( R.id.user_item_text_name );
        ImageButton goToMapButton = convertView.findViewById( R.id.friend_item_button_map);
        ImageButton removeFriend = convertView.findViewById( R.id.friend_item_button_remove);

        // Populate data into template.
        userName.setText( friend.getFirstName() + " " + friend.getLastName() );
        goToMapButton.setEnabled( false );
        removeFriend.setEnabled( false );

        return convertView;
    }
}
