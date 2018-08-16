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

public class FbDatabase {

    // An instance of the database.
    private FirebaseDatabase mDatabase;

    // A reference to our root node of the database.
    private DatabaseReference mReference;

    public FbDatabase() {
        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference();
    }

    public void setReferenceValue( String reference, Object value ) {
        mDatabase.getReference( reference ).setValue( value );
    }
}
