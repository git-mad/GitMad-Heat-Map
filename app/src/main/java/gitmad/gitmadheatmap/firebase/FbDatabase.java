package gitmad.gitmadheatmap.firebase;

import android.content.Context;
import android.content.SharedPreferences;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import gitmad.gitmadheatmap.AppContext;
import gitmad.gitmadheatmap.ILocationCallback;
import gitmad.gitmadheatmap.IRetrieveFriendsCallback;
import gitmad.gitmadheatmap.IRetrieveUserCallback;
import gitmad.gitmadheatmap.R;
import gitmad.gitmadheatmap.model.Friend;
import gitmad.gitmadheatmap.model.LocationInformation;
import gitmad.gitmadheatmap.model.User;

public class FbDatabase {

    // An instance of the database.
    private FirebaseDatabase database;

    // A reference to our root node of the database.
    private DatabaseReference reference;

    public FbDatabase() {
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
    }

    public void addLocation(LocationInformation locationInformation) {
        DatabaseReference myRef = database.getReference("locations/" + locationInformation.getUsername());
        myRef.setValue(locationInformation);
    }

    public void getLocations(final ILocationCallback ILocationCallback) {
        DatabaseReference myRef = database.getReference();

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<LatLng> locationList = new ArrayList<>();
                DataSnapshot locations = dataSnapshot.child("locations");
                Iterable<DataSnapshot> location_values = locations.getChildren();
                for (DataSnapshot location : location_values) {
                    double latitude = location.child("location").child("latitude").getValue( double.class );
                    double longitude = location.child("location").child("longitude").getValue( double.class );
                    locationList.add(new LatLng(latitude, longitude));
                }

                ILocationCallback.onFinish(locationList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /**
     * Retrieve information from Firebase about the selected user. This is mainly used to store shared preferences.
     *
     * @param username     The username of the user we wish to get from Firebase.
     * @param userCallback A callback to pass information back when our request is done.
     */
    public void getUser(final String username, final IRetrieveUserCallback userCallback) {
        DatabaseReference myRef = database.getReference();

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DataSnapshot user = dataSnapshot.child("users/" + username);
                String firstName = user.child("firstName").getValue(String.class);
                String lastName = user.child("lastName").getValue(String.class);
                String email = user.child("email").getValue(String.class);
                String hash = user.child("hash").getValue( String.class );
                String friends = user.child("friends").getValue( String.class );
                userCallback.onFinish(new User(firstName, lastName, email, hash, friends));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void getFriends(final IRetrieveFriendsCallback friendsCallback ) {
        SharedPreferences sharedPreferences = AppContext.getContext().getSharedPreferences(AppContext.getContext().getString(R.string.pref_preferences), Context.MODE_PRIVATE);
        String friendsString = sharedPreferences.getString( AppContext.getContext().getString( R.string.pref_user_friends ), "" );

        // If there are no friends then return. No need to do a db call.
        if( friendsString.equals( "" ) ) {
            friendsCallback.onFinish( new ArrayList<Friend>() );
            return;
        }

        final DatabaseReference myRef = database.getReference();
        final ArrayList<String> friends = new ArrayList<>(Arrays.asList(friendsString.split(",")));

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                ArrayList<Friend> friendArrayList = new ArrayList<>();
                for ( String friend : friends ) {
                    DataSnapshot dbFriend = dataSnapshot.child( "friends/" + friend );
                    String firstName = dbFriend.child("user/firstName").getValue(String.class);
                    String lastName = dbFriend.child("user/lastName").getValue(String.class);
                    String email = dbFriend.child("user/email").getValue(String.class);

                    friendArrayList.add( new Friend(firstName, lastName, email, friend) );
                }

                friendsCallback.onFinish( friendArrayList );
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void setReferenceValue(String reference, Object value) {
        database.getReference(reference).setValue(value);
    }
}
