package gitmad.gitmadheatmap;

import android.support.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FirebaseInstance {

    // An instance of the database.
    private FirebaseDatabase mDatabase;

    // A reference to our root node of the database.
    private DatabaseReference mReference;

    public FirebaseInstance() {
        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference();
    }

    public void addLocation( LatLng location ){
        DatabaseReference myRef = mDatabase.getReference( "locations" );
        myRef.push().setValue(location);
    }

    public void getLocations(final LocationCallback locationCallback ) {
        DatabaseReference myRef = mDatabase.getReference();

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<LatLng> location_list = new ArrayList<>();
                DataSnapshot locations = dataSnapshot.child( "locations" );
                Iterable<DataSnapshot> location_values = locations.getChildren();
                for ( DataSnapshot location : location_values ) {
                    double latitude = location.child("latitude").getValue( Double.class );
                    double longitude = location.child("longitude").getValue( Double.class );
                    location_list.add( new LatLng( latitude, longitude ) );
                }

                locationCallback.onFinish( location_list );

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
