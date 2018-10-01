package gitmad.gitmadheatmap.firebase;

import android.support.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import gitmad.gitmadheatmap.ILocationCallback;
import gitmad.gitmadheatmap.IRetrieveUserCallback;
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
                userCallback.onFinish(new User(firstName, lastName, email));
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
